package com.customers.zktc.base.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.binding.model.App
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

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
        Glide.with(imageView).load(url).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("android:src","radius")
    fun seImage(imageView: ImageView,url: String,radius: Int){
        val radiusDp = App.dipToPx(radius.toFloat())
        val options2 = RequestOptions()
            .centerCrop()
            .priority(Priority.HIGH)//优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
            .transform(RoundedCorners(radiusDp.toInt()))
        Glide.with(imageView.context)
            .load(url)
            .apply(options2)
            .into(imageView)
    }
}