package com.customers.zktc.inject.data.net.bean

import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewInflate
import com.customers.zktc.R
import com.customers.zktc.databinding.HolderUserLocationCityTitleBinding
import kotlinx.serialization.Serializable

@Serializable
data class CityTitleBean(
    val cityList: List<CityTitleEntity>
)
@Serializable
@LayoutView(R.layout.holder_user_location_city_title)
data class CityTitleEntity(
    val list: List<CityBean>,
    val title: String
):ViewInflate<HolderUserLocationCityTitleBinding>()

@Serializable
data class CityBean(
    val card: String,
    val name: String
)