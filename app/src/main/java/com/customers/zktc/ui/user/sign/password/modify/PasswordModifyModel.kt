package com.customers.zktc.ui.user.sign.password.modify

import android.os.Bundle
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentPasswordModifyBinding
import javax.inject.Inject

@LayoutView(layout= [R.layout.fragment_password_modify])
class PasswordModifyModel @Inject constructor():ViewModel<PasswordModifyFragment,FragmentPasswordModifyBinding>(){
    override fun attachView(savedInstanceState: Bundle?, t: PasswordModifyFragment) {
        super.attachView(savedInstanceState, t)
    }

}
