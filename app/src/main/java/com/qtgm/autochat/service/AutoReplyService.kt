package com.qtgm.autochat.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent.CanceledException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.qtgm.autochat.constant.Constants
import com.qtgm.autochat.constant.GlobalConfig
import com.qtgm.base.utils.MsLog
import com.qtgm.base.utils.PhoneController

class AutoReplyService : AccessibilityService() {

    private val TAG: String = this.javaClass.getSimpleName()

    companion object {
        private var sIsBound = false
        val isConnected: Boolean
            get() = sIsBound
    }

    private val handler = Handler(Looper.getMainLooper())

    private var hasNotify = false

    /**
     * 必须重写的方法，响应各种事件。
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!GlobalConfig.isAutoReply) {
            MsLog.i(TAG, "interrupt auto reply service")
            return
        }
        val eventType = event.eventType // 事件类型
        when (eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                MsLog.i(TAG, "TYPE_NOTIFICATION_STATE_CHANGED")
                if (PhoneController.isLockScreen(this)) { // 锁屏
                    PhoneController.wakeAndUnlockScreen(this) // 唤醒点亮屏幕
                }
                openAppByNotification(event)
                hasNotify = true
            }
            else -> {
                MsLog.i(TAG, "DEFAULT")
                if (hasNotify) {
                    try {
                        Thread.sleep(1000) // 停1秒, 否则在微信主界面没进入聊天界面就执行了fillInputBar
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    if (fillInputBar(GlobalConfig.replyText1)) {
                        findAndPerformAction(Constants.UI_BUTTON, "发送")
                        handler.postDelayed({
                            performGlobalAction(GLOBAL_ACTION_BACK) // 返回
                        }, 1500)
                    }
                    hasNotify = false;
                }
            }
        }
    }

    override fun onInterrupt() {
        MsLog.i(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        // mainfest 配置了这里无需配置
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        info.packageNames = new String[]{Config.WX_PACKAGE_NAME};
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        info.notificationTimeout = 100;
//        this.setServiceInfo(info);
        MsLog.i(TAG, "connect auto reply service")
        sIsBound = true
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent): Boolean {
        MsLog.i(
            TAG,
            "disconnect auto reply service"
        )
        sIsBound = false
        return super.onUnbind(intent)
    }

    /**
     * 查找UI控件并点击
     * @param widget 控件完整名称, 如android.widget.Button, android.widget.TextView
     * @param text 控件文本
     */
    private fun findAndPerformAction(widget: String, text: String) {
        // 取得当前激活窗体的根节点
        if (rootInActiveWindow == null) {
            return
        }

        // 通过文本找到当前的节点
        val nodes = rootInActiveWindow.findAccessibilityNodeInfosByText(text)
        if (nodes != null) {
            for (node in nodes) {
                if (node.className == widget && node.isEnabled) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK) // 执行点击
                    break
                }
            }
        }
    }

    /**
     * 打开微信
     * @param event 事件
     */
    private fun openAppByNotification(event: AccessibilityEvent) {
        if (event.parcelableData != null && event.parcelableData is Notification) {
            val notification = event.parcelableData as Notification
            try {
                val pendingIntent = notification.contentIntent
                pendingIntent.send()
            } catch (e: CanceledException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 填充输入框
     */
    private fun fillInputBar(reply: String): Boolean {
        val rootNode = rootInActiveWindow
        return rootNode?.let { findInputBar(it, reply) } ?: false
    }

    /**
     * 查找EditText控件
     * @param rootNode 根结点
     * @param reply 回复内容
     * @return 找到返回true, 否则返回false
     */
    private fun findInputBar(rootNode: AccessibilityNodeInfo, reply: String): Boolean {
        val count = rootNode.childCount
        MsLog.i(
            TAG,
            "root class=" + rootNode.className + ", " + rootNode.text + ", child: " + count
        )
        for (i in 0 until count) {
            val node = rootNode.getChild(i)
            if (Constants.UI_EDITTEXT.equals(node.className)) {   // 找到输入框并输入文本
                MsLog.i(
                    TAG,
                    "****found the EditText"
                )
                fillText(node, reply)
                return true
            }
            if (findInputBar(node, reply)) {    // 递归查找
                return true
            }
        }
        return false
    }

    /**
     * 填充文本
     */
    private fun fillText(node: AccessibilityNodeInfo, reply: String) {
        MsLog.i(TAG, "set text")
        val args = Bundle()
        args.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            reply
        )
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
    }


}