package com.binding.model.adapter.databinding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

/**
 * Created by arvin on 2018/1/17.
 */
@SuppressLint("RestrictedApi")
object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:drawableBottom")
    fun setDrawableBottom(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableBottom(view, ContextCompat.getDrawable(view.context, drawable)
        )
    }
    @JvmStatic
    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableLeft(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )

    }
    @JvmStatic
    @BindingAdapter("android:drawableRight")
    fun setDrawableRight(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableRight(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )
    }
    @JvmStatic
    @BindingAdapter("android:drawableTop")
    fun setDrawableTop(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableTop(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )

    }

    @BindingAdapter("android:drawableStart")
    fun setDrawableStart(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableStart(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )
    }

    @BindingAdapter("android:drawableEnd")
    fun setDrawableEnd(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter
            .setDrawableEnd(view, ContextCompat.getDrawable(view.context, drawable))
    }
}
