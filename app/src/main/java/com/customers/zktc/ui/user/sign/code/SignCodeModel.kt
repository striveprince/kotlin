package com.customers.zktc.ui.user.sign.code

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.busPost
import com.binding.model.inflate.model.ViewModel
import com.binding.model.rxBus
import com.customers.zktc.R
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentSignCodeBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.user.sign.SignEvent
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.user.sign.login.LoginFragment.Companion.login
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_sign_code])
class SignCodeModel @Inject constructor() : ViewModel<SignCodeFragment, FragmentSignCodeBinding>() {
    val enablePhone = ObservableBoolean(false)
    @Inject
    lateinit var api: Api

    override fun attachView(savedInstanceState: Bundle?, t: SignCodeFragment) {
        super.attachView(savedInstanceState, t)
        bindingParams(t)
        binding?.codeView?.codeListener = { if (it.length == 4) loginCode(it) }
    }

    private fun bindingParams(t: SignCodeFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding?.params = it }
        addDisposables(rxBus<SignEvent>(t).subscribe({
            binding?.params = it.signParams
        }, { it.printStackTrace() }))
    }

    fun onInputFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }

    fun onPasswordLoginClick(v: View) {
        busPost(SignEvent(login, binding!!.params!!))
    }

    fun onCodeClick(v: View) {
        addDisposables(api.code().subscribe())
    }

    private fun loginCode(code: String) {
        binding?.params?.let {
            it.smsCode = code
            addDisposables(api.codeLogin(it).subscribe())
        }
    }
}
