package com.customers.zktc.ui.home.mine

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.arouter.ARouterUtil
import com.customers.zktc.base.util.string
import com.customers.zktc.databinding.FragmentHomeMineBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.inject.data.preference.user.UserApi
import com.customers.zktc.ui.receiveLoginEvent
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineModel @Inject constructor() : ViewModel<HomeMineFragment, FragmentHomeMineBinding>() {
    @Inject
    lateinit var api: Api
    val messageCount = ObservableField<String>()
    val name = ObservableField<String>()

    override fun attachView(savedInstanceState: Bundle?, t: HomeMineFragment) {
        super.attachView(savedInstanceState, t)
        receiveLoginEvent().subscribeNormal(t, { initUser(it.login) })
        initUser(UserApi.isLogin)
    }

    private fun initUser(login: Boolean) {
        binding.bean = api.userBean()
        if (login) {
            api.getUnReadMessage().subscribeNormal({
                if (it != 0) {

                }
                messageCount.set(messageCountToString(it))
            })
            name.set(binding.bean?.customerNickname)
            api.rank().subscribeNormal({})//好友数据
            api.getCustomerIndexInfo().subscribeNormal({})//待发货
        }else{
            name.set(R.string.login_and_register.string())
        }
    }

    private fun messageCountToString(it: Int) = if (it == 0) "" else it.toString()

    fun onSettingClick(view: View){
        ARouterUtil.setting(view)
    }
}
