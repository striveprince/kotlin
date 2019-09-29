package com.binding.model.base.container

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.binding.model.R
import com.binding.model.base.view.SwipeBackLayout
import com.binding.model.base.view.SwipeBackLayout.Companion.FROM_LEFT

abstract class DataBindingActivity<C> : AppCompatActivity(), CycleContainer<C> {
    override val dataActivity = this
    override val cycle= lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    fun isSwipe() = FROM_LEFT
}
