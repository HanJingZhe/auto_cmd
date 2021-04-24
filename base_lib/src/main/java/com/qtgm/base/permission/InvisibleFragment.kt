package com.qtgm.base.permission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

class InvisibleFragment : Fragment() {

    private var callback: ((Boolean, List<String>) -> Unit?)? = null

    fun requestNow(cb: (Boolean, List<String>) -> Unit, vararg permissions: String) {
        callback = cb
        requestPermissions(permissions, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            val deniedList = ArrayList<String>()
            grantResults.forEachIndexed { index, result ->
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[index])
                }
            }
            callback?.let { it(deniedList.isEmpty(), deniedList) }
        }

    }

}