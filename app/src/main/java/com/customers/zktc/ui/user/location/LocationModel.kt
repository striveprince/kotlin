package com.customers.zktc.ui.user.location

import android.os.Bundle
import com.binding.model.adapter.EventEntity
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.inter.Inflate
import com.binding.model.inflate.model.RecyclerModel
import com.customers.zktc.R
import com.customers.zktc.databinding.ActivityLocationBinding
import com.customers.zktc.inject.data.Api
import javax.inject.Inject

@LayoutView(R.layout.activity_location)
class LocationModel @Inject constructor() :RecyclerModel<LocationActivity,ActivityLocationBinding,Inflate<*>> (){
    @Inject lateinit var api:Api
    override fun attachView(savedInstanceState: Bundle?, t: LocationActivity) {
        super.attachView(savedInstanceState, t)
        setRxHttp { _, _ ->  api.cities()}
        adapter.addEventAdapter{ _,e,_,_ -> api.location(e).map{ EventEntity<Any>(it) } }
        api.locationCurrent(t)
    }
}
