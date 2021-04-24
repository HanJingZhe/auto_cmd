package com.qtgm.autochat.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.qtgm.autochat.constant.Constants
import com.qtgm.base.utils.MsLog

class TestAccessibilityService: AccessibilityService() {

    private val TAG = this.javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        MsLog.i(TAG, "$TAG is create()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MsLog.i(TAG, "$TAG is onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        MsLog.i(TAG, "$TAG is onDestroy()")
        super.onDestroy()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val asInfo = AccessibilityServiceInfo()
        asInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        asInfo.packageNames = arrayOf(Constants.WX_PACKAGE_NAME)
        asInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
        asInfo.notificationTimeout = 100
        this.serviceInfo = asInfo

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        printEventLog(event)
    }

    override fun onInterrupt() {
    }


    private fun printEventLog(event: AccessibilityEvent) {
        MsLog.i(TAG, "-------------------------------------------------------------")
        val eventType = event.eventType //事件类型
        MsLog.i(TAG, "PackageName:" + event.packageName + "") // 响应事件的包名
        MsLog.i(TAG, "Source Class:" + event.className + "") // 事件源的类名
        MsLog.i(TAG, "Description:" + event.contentDescription + "") // 事件源描述
        MsLog.i(TAG, "Event Type(int):$eventType")
        when (eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> MsLog.i(
                TAG,
                "event type:TYPE_NOTIFICATION_STATE_CHANGED"
            )
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> MsLog.i(
                TAG,
                "event type:TYPE_WINDOW_STATE_CHANGED"
            )
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> MsLog.i(
                TAG,
                "event type:TYPE_VIEW_ACCESSIBILITY_FOCUSED"
            )
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> MsLog.i(
                TAG,
                "event type:TYPE_VIEW_ACCESSIBILITY_FOCUSED"
            )
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> MsLog.i(
                TAG,
                "event type:TYPE_GESTURE_DETECTION_END"
            )
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> MsLog.i(
                TAG,
                "event type:TYPE_WINDOW_CONTENT_CHANGED"
            )
            AccessibilityEvent.TYPE_VIEW_CLICKED -> MsLog.i(TAG, "event type:TYPE_VIEW_CLICKED")
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> MsLog.i(
                TAG,
                "event type:TYPE_VIEW_TEXT_CHANGED"
            )
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> MsLog.i(TAG, "event type:TYPE_VIEW_SCROLLED")
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> MsLog.i(
                TAG,
                "event type:TYPE_VIEW_TEXT_SELECTION_CHANGED"
            )
            else -> MsLog.i(TAG, "no listen event")
        }
        for (txt in event.text) {
            MsLog.i(TAG, "text:$txt")
        }
        MsLog.i(TAG, "-------------------------------------------------------------")
    }


}