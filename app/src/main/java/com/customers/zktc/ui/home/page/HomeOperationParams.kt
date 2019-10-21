package com.customers.zktc.ui.home.page

import com.customers.zktc.inject.data.net.converter.ApiParams
import kotlinx.serialization.Serializable

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/16 17:52
 * Email: 1033144294@qq.com
 */
data class HomeOperationParams(val adPositionNumber: String) : ApiParams

data class HomeFloorParams(val floorClassifyNumber: String) : ApiParams

@Serializable
data class HomeRecommendParams(
    val siteNumber: String,
    val pageNo: Int, val pageNum: Int, val pageSize: Int
) : ApiParams
 class HomeRushListParams:ApiParams
data class HomeRushParams(
    val pageNo: Int = 0,
    val storeId: Int = 0,
    val customerId: Int = 0,
    val pageSize: Int = 0,
    val marketingIds: Array<Int>
) :ApiParams