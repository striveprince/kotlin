package com.customers.zktc.ui.home.cart

import android.os.Bundle
import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.model.RecyclerModel
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeCartBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.inject.data.preference.user.UserApi
import com.customers.zktc.ui.receiveLoginEvent
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_cart])
class HomeCartModel @Inject constructor() :
    RecyclerModel<CycleContainer<*>, FragmentHomeCartBinding, HomeCartStoreEntity>() {
    @Inject lateinit var api: Api

    override fun attachView(savedInstanceState: Bundle?, t: CycleContainer<*>) {
        super.attachView(savedInstanceState, t)
        receiveLoginEvent().subscribeNormal(t, { initCart(it.login) })
        initCart(UserApi.isLogin)
    }

    private fun initCart(it: Boolean) {
        if (it) {
            setRxHttp { _, _ -> api.shoppingCartList() }
        } else {
            adapter.refreshListAdapter(0,ArrayList())
        }
    }
}
