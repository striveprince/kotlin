package com.customers.zktc.ui.user.login

import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.ActivityLoginBinding
import javax.inject.Inject

@LayoutView(layout=[R.layout.activity_login])
class LoginModel @Inject constructor():ViewModel<LoginActivity,ActivityLoginBinding>(){

}
