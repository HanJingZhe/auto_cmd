package com.qtgm.base.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import com.qtgm.base.R
import com.qtgm.base.dialog.MsLoadingDialog
import com.qtgm.base.utils.MsLog
import kotlinx.android.synthetic.main.base_title_layout.*
import java.util.jar.Attributes

/**
 * @author peng.wang08
 * @date 2021/1/29
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mContext: Context
    protected val TAG: String = this.javaClass.simpleName

    protected val msLoadingDialog: MsLoadingDialog by lazy { MsLoadingDialog(mContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(setLayoutId())
        setStatusBar()
        initView()
        initData()
    }

    abstract fun setLayoutId(): Int
    abstract fun initView()
    abstract fun initData()

    override fun onDestroy() {
        msLoadingDialog.dismiss()
        super.onDestroy()
    }

    open fun setStatusBar(color: Int = Color.TRANSPARENT, dark: Boolean = false) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color

        if (toolBar != null) {
            toolBar.setPadding(0, getStatusBarHeight(), 0, 0)
        }
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 处理点击缩回软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view != null && view is EditText) {
                val leftTop = intArrayOf(0, 0)
                view.getLocationInWindow(leftTop)
                val left = leftTop[0]
                val top = leftTop[1]
                val bottom = top + view.height
                val right = left + view.width
                if (ev.x > left && ev.x < right && ev.y > top && ev.y < bottom) {
                    //点击的是正在操作的EditText,保留点击事件
                } else {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        if (window.superDispatchTouchEvent(ev)) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }

}