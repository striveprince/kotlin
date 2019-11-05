package com.customers.zktc.ui.user.location

import android.view.View
import androidx.databinding.ViewDataBinding
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewEntity
import com.customers.zktc.R
import com.customers.zktc.inject.data.net.bean.CityBean

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/5 12:49
 * Email: 1033144294@qq.com
 */
@LayoutView(R.layout.holder_user_location_city)
class LocationEntity(bean : CityBean):ViewEntity<CityBean,ViewDataBinding>(bean){
    fun onCityClick(v:View){

    }
}