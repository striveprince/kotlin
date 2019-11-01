package com.customers.zktc.ui.user.sign.login

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.util.getPasswordError
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentLoginBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.loginEvent
import com.customers.zktc.ui.receiveSignEvent
import com.customers.zktc.ui.signEvent
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
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding.params = it }
//        rxBus<SignEvent>(t).subscribeNormal(this, { binding.params = it.signParams })
        receiveSignEvent().subscribeNormal(t)
    }

    fun onPhoneFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding.inputEditMobile.error = getPhoneError(s.toString())
    }

    fun onPasswordFinish(s: Editable) {
        enablePassword.set(getPasswordError(s.toString()) == null)
        binding.inputEditPassword.error = getPasswordError(s.toString())
    }

    fun onForgetClick(v: View) {
        signEvent(forget, binding.params!!)
    }

    fun onWechatClick(v: View) {
        api.wechatLogin(binding.params!!)
            .subscribeNormal(v,t.lifecycle,{
        })
    }

    fun onLoginClick(v: View) {
        api.passwordLogin(binding.params!!)
            .subscribeNormal(v,t.lifecycle,{
                loginEvent(true,it)
                finish()})
    }

    fun onCodeClick(v: View) {
        signEvent(signCode, binding.params!!)
    }

    fun onRegisterClick(v: View) {
        signEvent(register, binding.params!!)
    }

}
