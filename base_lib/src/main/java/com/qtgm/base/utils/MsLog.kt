package com.qtgm.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.qtgm.base.BuildConfig
import java.io.BufferedWriter
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object MsLog {

    private val TAG = this.javaClass.simpleName

    private const val MAXLENGTH = 1024 * 3
    private val ISDEBUG = BuildConfig.DEBUG
    private val ISWRITE = false

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        log(Level.D, tag, msg)
    }

    fun i(msg: String) {
        d(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        log(Level.I, tag, msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        log(Level.E, tag, msg)
    }

    private fun log(level: Level, tag: String, msg: String) {
        var str = msg
        val nTag = if (tag == TAG) TAG else "$TAG-$tag"

        while (str.length > MAXLENGTH) {
            when (level) {
                Level.D -> Log.d("$nTag=====================", str.substring(0, MAXLENGTH))
                Level.I -> Log.i("$nTag=====================", str.substring(0, MAXLENGTH))
                Level.E -> Log.e("$nTag=====================", str.substring(0, MAXLENGTH))
            }
            if (ISWRITE) write(level, nTag, str)
            str = str.substring(MAXLENGTH)
        }

        when (level) {
            Level.D -> Log.d("$nTag=====================", str)
            Level.I -> Log.i("$nTag=====================", str)
            Level.E -> Log.e("$nTag=====================", str)
        }
        if (ISWRITE) write(level, nTag, str)
    }

    private fun write(level: Level, tag: String, msg: String) {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val content = "${sdf.format(date)} level=$level $tag : $msg"
        val bw = BufferedWriter(FileWriter("/sdcard/qtgm/peng.log", true))
        bw.write(content)
        bw.newLine()
        bw.flush()
    }

    enum class Level {
        D, I, E
    }

}