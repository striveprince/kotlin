package com.customers.zktc.ui.user.sign.password.forget

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentPasswordForgetBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.receiveSignEvent
import com.customers.zktc.ui.signEvent
import com.customers.zktc.ui.user.sign.login.LoginFragment
import javax.inject.Inject


@LayoutView(layout=[R.layout.fragment_password_forget])
class PasswordForgetModel @Inject constructor(): ViewModel<PasswordForgetFragment,FragmentPasswordForgetBinding>() {
    val enablePhone = ObservableBoolean(false)
    val enableCodeClick = ObservableBoolean(false)
    val enableCode = ObservableBoolean(false)
    val enablePassword = ObservableBoolean(false)

    @Inject lateinit var api:Api
    override fun attachView(savedInstanceState: Bundle?, t: PasswordForgetFragment) {
        super.attachView(savedInstanceState, t)
        bindingParams(t)
    }

    private fun bindingParams(t: PasswordForgetFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding?.params = it }
        receiveSignEvent().subscribeNormal(this,{ binding?.params = it.signParams })
    }

    fun onPhoneFinish(s:Editable){
        enablePhone.set(getPhoneError(s.toString())==null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }

    fun onCodeFinish(s:Editable){
        enableCode.set(getPhoneError(s.toString())==null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }


    fun onCodeClick(v:View){
        enableCodeClick.set(true)
    }

    fun onConfirmClick(v:View){
        api.modifyPassword().subscribeNormal(t)
    }

    fun onPasswordFinish(s:Editable){
        binding?.params?.let { signEvent(LoginFragment.login,it) }
    }
}
