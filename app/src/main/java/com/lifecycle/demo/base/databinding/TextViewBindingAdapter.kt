package com.lifecycle.demo.base.databinding

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lifecycle.demo.base.util.headUrl
import kotlin.math.min

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/23 16:03
 * Email: 1033144294@qq.com
 */
object TextViewBindingAdapter {


    @JvmStatic
    @BindingAdapter("android:drawableTop")
    fun setDrawableTop(view: TextView, url: String) {
        val mContext = view.context
        val options = RequestOptions()
            .centerCrop()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(mContext)
            .load(headUrl(url))
            .apply(options)
            .into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(drawable: Drawable, transition: Transition<in Drawable>?) {
                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                val drawables = view.compoundDrawables
                view.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3])
            }
        })
    }

    @JvmStatic
    @BindingAdapter("drawableLeftCircle")
    fun drawableCircleLeft(view: TextView, url: String) {
        if (!TextUtils.isEmpty(url)) {
            val context = view.context
            val options = RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(CircleCrop())
            Glide.with(context).load(headUrl(url)).apply(options)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        val size = min(view.height, view.width)
                        resource.setBounds(0, 0, size, size)
                        val drawables = view.compoundDrawables
                        view.setCompoundDrawables(resource, drawables[1], drawables[2], drawables[3])
                    }
                })
        }
    }


}