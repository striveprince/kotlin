package com.lifecycle.binding.base.container

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.binding.*
import com.lifecycle.binding.base.view.SwipeBackLayout
import com.lifecycle.binding.base.view.SwipeBackLayout.Companion.FROM_LEFT
import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.demo.R

abstract class DataBindingActivity<Model:ViewModel,Binding : ViewDataBinding> : AppCompatActivity(),
    Inflate<Binding> {
    lateinit var binding: Binding
    override fun binding() = binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
    }

    open fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        binding = attachContainer(this, null, false, null)
        if (isSwipe() != SwipeBackLayout.FROM_NO) {
            setContentView(R.layout.activity_base)
            val swipeBackLayout: SwipeBackLayout = findViewById(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            val p = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            swipeBackLayout.addView(binding.root, p)
            val imageView: ImageView = findViewById(R.id.iv_shadow)
            swipeBackLayout.setOnSwipeBackListener { _, f -> imageView.alpha = 1 - f }
        } else setContentView(binding.root)
        binding.lifecycleOwner = this
        applyKitKatTranslucency(this, android.R.color.transparent)
    }

    open fun isSwipe() = FROM_LEFT
    /**
     * modify this status text color and background
     * */
    @SuppressLint("PrivateApi")
    private fun applyKitKatTranslucency(activity: AppCompatActivity, color: Int) {
        setMeizuStatusBarDarkIcon(activity, true)
        setMiuiStatusBarDarkMode(activity, true)
        setAllUpSixVersion(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(window.decorView, Color.TRANSPARENT)  //改为透明
            } catch (e: Exception) {
            }
        }
        val tintManager = SystemBarTintManager(activity)
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
