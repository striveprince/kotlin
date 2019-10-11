package com.customers.zktc.ui.home.page

import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.ViewInflate

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:15
 * Email: 1033144294@qq.com
 */

data class HomePageBannerEntity (
    val id:Int,
    val name:String,
    val picture:String,
    val viceName:String,
    val orderBy:Int,
    val linkUrl:String
):ViewInflate<ViewDataBinding>()