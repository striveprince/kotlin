package com.binding.model.base.container

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.customers.zktc.R
import com.binding.model.base.view.SwipeBackLayout
import com.binding.model.base.view.SwipeBackLayout.Companion.FROM_LEFT
import com.binding.model.setAllUpSixVersion
import com.binding.model.setMeizuStatusBarDarkIcon
import com.binding.model.setMiuiStatusBarDarkMode

abstract class DataBindingActivity<C> : AppCompatActivity(), CycleContainer<C> {
    override val dataActivity = this
    override val cycle= lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        val rootView = inject(savedInstanceState, null, false)
        if (isSwipe() != SwipeBackLayout.FROM_NO) {
            setContentView(R.layout.activity_base)
            val swipeBackLayout:SwipeBackLayout = findViewById(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            swipeBackLayout.addView(
                rootView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
            val imageView:ImageView = findViewById(R.id.iv_shadow)
            swipeBackLayout.setOnSwipeBackListener { mView, f -> imageView.alpha = 1 - f }

        } else
            setContentView(rootView)

        applyKitKatTranslucency(android.R.color.transparent)
    }

    open fun isSwipe() = FROM_LEFT


    private fun applyKitKatTranslucency(color: Int) {
        //确定状态栏的字体演示是否为黑色,false为白色，true为黑色，目前6.0以下仅仅适配了两个机型
        setMeizuStatusBarDarkIcon(this, true)
        setMiuiStatusBarDarkMode(this, true)
        //6.0以上系统
        setAllUpSixVersion(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true)
        }
        //6.0以上，修改状态栏为透明色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(window.decorView, Color.TRANSPARENT)  //改为透明
            } catch (e: Exception) {
            }

        }
        val tintManager = SystemBarTintManager(this)
        tintManager.isStatusBarTintEnabled = true
        tintManager.setStatusBarTintResource(color)
    }


    @TargetApi(19)
    private fun setTranslucentStatus(on: Boolean) {
        val win = window
        val winParams = win.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}
