package com.customers.zktc.ui.home.page

import com.customers.zktc.inject.data.net.converter.ApiParams

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/16 17:52
 * Email: 1033144294@qq.com
 */
data class HomeOperationParams (val adPositionNumber:String):ApiParams
data class HomeFloorParams (val floorClassifyNumber:String):ApiParams
data class HomeRecommendParams (val siteNumber:String,
                               val pageNo:Int,val pageNum:Int,val pageSize:Int):ApiParams
//{"siteNumber":"goods_home_index_1","pageNo":"1","pageNum":"1","pageSize":"200"}