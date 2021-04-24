package com.qtgm.base.utils

import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager

object PhoneController {

    /**
     * @return 屏幕是否处于锁屏状态
     * true:亮, a.未锁屏 b.处于解锁状态
     * false:暗 屏幕黑的
     */
    fun isLockScreenByPower(ctx: Context): Boolean {
        val pm = ctx.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isInteractive
    }

    /**
     * @return 屏幕是否处于锁屏状态
     * true a.屏幕黑的 b.处于解锁状态
     * false 目前未解锁
     */
    fun isLockScreen(ctx: Context): Boolean {
        val km = ctx.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return km.isKeyguardLocked()
    }

    /**
     * 唤醒手机并解锁(不能有锁屏密码)
     * @param context
     */
    fun wakeAndUnlockScreen(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP
                    or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright"
        )
        wakeLock.acquire(1000) // 点亮屏幕
        wakeLock.release()
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val lock = km.newKeyguardLock("unlock")
        lock.disableKeyguard() // 解锁
    }

}