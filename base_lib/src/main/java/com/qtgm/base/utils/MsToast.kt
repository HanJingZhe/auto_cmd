package com.qtgm.base.utils

import android.content.Context
import android.widget.Toast
import com.qtgm.base.BuildConfig

object MsToast {

    private val ISDEBUG = BuildConfig.DEBUG
    private lateinit var mContext: Context

    fun initToast(ctx: Context) {
        mContext = ctx
    }

    fun toastS(msg: String) {
        if (!ISDEBUG) return
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastL(msg: String) {
        if (!ISDEBUG) return
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

}