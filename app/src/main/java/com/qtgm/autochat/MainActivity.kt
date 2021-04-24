package com.qtgm.autochat

import com.qtgm.base.base.BaseActivity
import com.qtgm.base.utils.MsLog

class MainActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

    }

    override fun initData() {
    }

    override fun onDestroy() {
        MsLog.i(TAG,"$TAG is onDestroy")
        super.onDestroy()
    }

}