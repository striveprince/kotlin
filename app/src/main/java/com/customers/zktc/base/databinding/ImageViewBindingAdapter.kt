package com.customers.zktc.base.databinding

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.binding.model.App
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.customers.zktc.base.util.headUrl
import kotlin.math.min

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/23 10:42
 * Email: 1033144294@qq.com
 */
object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImage(imageView: ImageView,url:String){
        val options2 = RequestOptions()
            .priority(Priority.HIGH)//优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
        Glide.with(imageView)
            .load(url)
            .apply(options2)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("android:src","radius")
    fun seImage(imageView: ImageView,url: String,radius: Int){
        val radiusDp = App.dipToPx(radius.toFloat()).toInt()
        val options2 = RequestOptions()
            .priority(Priority.HIGH)//优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
            .fitCenter()
            .transform(RoundedCorners(radiusDp))
        Glide.with(imageView.context)
            .load(url)
            .apply(options2)
            .into(imageView)
    }




}