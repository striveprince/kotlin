package com.customers.zktc.ui.user.address

import android.os.Bundle
import android.view.View
import com.binding.model.annoation.LayoutView
import com.binding.model.base.rotate.TimeUtil
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.binding.model.toast
import com.customers.zktc.R
import com.customers.zktc.base.adapter.FilterAdapter
import com.customers.zktc.databinding.ActivityAddressBinding
import com.customers.zktc.inject.data.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 9:46
 * Email: 1033144294@qq.com
 */
@LayoutView(layout = [R.layout.activity_address])
class AddressModel @Inject constructor() : ViewModel<AddressActivity, ActivityAddressBinding>() {
    @Inject lateinit var api:Api

    override fun attachView(savedInstanceState: Bundle?, t: AddressActivity) {
        super.attachView(savedInstanceState, t)
        api.searchGeoCode(t){binding.autoComplete.setText(it)}
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeNormal ({
                val adapter =  FilterAdapter<MapEntity>()
                adapter.addListAdapter(0,it)
                binding.autoComplete.setAdapter(adapter)
                binding.autoComplete.showDropDown()
            })
        TimeUtil.handler.postDelayed({ binding.autoComplete.showDropDown() }, 100)
    }

    fun onLocationClick(v:View){
        binding.autoComplete.showDropDown()
    }
}