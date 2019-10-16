package com.customers.zktc.ui.home.cart

import android.os.Bundle
import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.model.ViewModel
import com.binding.model.rxBus
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeCartBinding
import com.customers.zktc.inject.data.preference.user.UserApi
import com.customers.zktc.ui.user.sign.login.LoginEvent
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_cart],event = true)
class HomeCartModel @Inject constructor(): ViewModel<CycleContainer<*>, FragmentHomeCartBinding>(){

    override fun attachView(savedInstanceState: Bundle?, t: CycleContainer<*>) {
        super.attachView(savedInstanceState, t)
        addDisposables(rxBus<LoginEvent>().subscribe{initCart(it.login)})
        initCart(UserApi.isLogin)
    }

    private fun initCart(it: Boolean) {
        if(it){

        }else{

        }
    }
}
