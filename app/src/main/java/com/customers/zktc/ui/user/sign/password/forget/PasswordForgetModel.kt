package com.customers.zktc.ui.user.sign.password.forget

import android.os.Bundle
import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.Container
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentPasswordForgetBinding
import javax.inject.Inject


@LayoutView(layout=[R.layout.fragment_password_forget])
class PasswordForgetModel @Inject constructor(): ViewModel<PasswordForgetFragment,FragmentPasswordForgetBinding>() {

    override fun attachView(savedInstanceState: Bundle?, t: PasswordForgetFragment) {
        super.attachView(savedInstanceState, t)

    }
}
