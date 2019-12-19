package com.lifecycle.demo.base.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lifecycle.binding.App

/**
 * Company:
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
    fun seImage(imageView: ImageView, url: String, radius: Int){
        val radiusDp = App.floatToPx(radius.toFloat()).toInt()
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

    @JvmStatic
    @BindingAdapter("srcCompat","radius")
    fun seImageWrap(imageView: ImageView,url: String,radius: Int){
        val radiusDp = App.floatToPx(radius.toFloat()).toInt()
        val options2 = RequestOptions()
            .priority(Priority.HIGH)//优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
            .fitCenter()
            .transform(RoundedCorners(radiusDp))
        Glide.with(imageView.context)
            .load(url)
            .apply(options2)
            .into(imageView)
//        Glide.with(imageView.context)
//            .load(url)
//            .apply(options2)
//            .into(object : SimpleTarget<Drawable>() {
//                override fun onResourceReady(drawable: Drawable, transition: Transition<in Drawable>?) {
////                    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
//                    Timber.i("intrinsicWidth=${drawable.intrinsicWidth},intrinsicHeight=${drawable.intrinsicHeight}")
//                    val layoutParams = imageView.layoutParams
//                    if(imageView.layoutParams.height == -1){
//                        layoutParams.height = imageView.layoutParams.width*drawable.intrinsicHeight/drawable.intrinsicWidth
//                    }else if(imageView.layoutParams.width == -1){
//                        layoutParams.width = imageView.layoutParams.height*drawable.intrinsicWidth/drawable.intrinsicHeight
//                    }
//                    imageView.layoutParams = layoutParams
//                    imageView.setImageDrawable(drawable)
//                }
//            })
    }


}