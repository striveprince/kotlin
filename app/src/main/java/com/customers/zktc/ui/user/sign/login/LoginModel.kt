package com.customers.zktc.ui.user.sign.login

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.busPost
import com.binding.model.inflate.model.ViewModel
import com.binding.model.rxBus
import com.binding.model.subscribeApi
import com.customers.zktc.R
import com.customers.zktc.base.util.getPasswordError
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentLoginBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.user.sign.SignEvent
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.user.sign.code.SignCodeFragment.Companion.signCode
import com.customers.zktc.ui.user.sign.password.forget.PasswordForgetFragment.Companion.forget
import com.customers.zktc.ui.user.sign.register.RegisterFragment.Companion.register
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_login])
class LoginModel @Inject constructor() : ViewModel<LoginFragment, FragmentLoginBinding>() {
    val enablePhone = ObservableBoolean(false)
    val enablePassword = ObservableBoolean(false)
    @Inject lateinit var api:Api
    override fun attachView(savedInstanceState: Bundle?, t: LoginFragment) {
        super.attachView(savedInstanceState, t)
        bindingParams(t)
    }

    private fun bindingParams(t: LoginFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding?.params = it }
        rxBus<SignEvent>(t).subscribeApi(t) { binding?.params = it.signParams }
    }

    fun onPhoneFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }

    fun onPasswordFinish(s: Editable) {
        enablePassword.set(getPasswordError(s.toString()) == null)
        binding?.inputEditPassword?.error = getPasswordError(s.toString())
    }

    fun onForgetClick(v: View) {
        busPost(SignEvent(forget, binding!!.params!!))
    }

    fun onWechatClick(v: View) {
        api.wechatLogin(binding!!.params!!).subscribeApi(t)
    }

    fun onLoginClick(v: View) {
        api.passwordLogin(binding!!.params!!).subscribeApi(t)
    }

    fun onCodeClick(v: View) {
        busPost(SignEvent(signCode, binding!!.params!!))
    }

    fun onRegisterClick(v: View) {
        busPost(SignEvent(register, binding!!.params!!))
    }

}
