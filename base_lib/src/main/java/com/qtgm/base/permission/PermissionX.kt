package com.qtgm.base.permission

import androidx.fragment.app.FragmentActivity

object PermissionX {

    private const val TAG = "InvisibleFragment"

    fun requestPermission(
        act: FragmentActivity,
        vararg permissions: String,
        cb: (Boolean, List<String>) -> Unit
    ) {
        val fm = act.supportFragmentManager
        val existedFragment = fm.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null) {
            existedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fm.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        fragment.requestNow(cb, *permissions)
    }
}