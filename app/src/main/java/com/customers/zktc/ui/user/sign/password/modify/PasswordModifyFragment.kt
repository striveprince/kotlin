package com.customers.zktc.ui.user.sign.password.modify

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.inject.component.FragmentComponent
import com.customers.zktc.ui.user.sign.password.modify.PasswordModifyFragment.Companion.modify

@Route(path = modify)
class PasswordModifyFragment:BaseFragment<PasswordModifyModel>(){
    companion object{ const val modify = FragmentComponent.Config.fragment +"modify"}

}