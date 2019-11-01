package com.customers.zktc.ui.user.sign.code

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.base.rotate.TimeUtil
import com.binding.model.base.rotate.TimingEntity
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.binding.model.toast
import com.customers.zktc.R
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.base.util.showInputMethod
import com.customers.zktc.databinding.FragmentSignCodeBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.*
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.user.sign.login.LoginFragment.Companion.login
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_sign_code])
class SignCodeModel @Inject constructor() : ViewModel<SignCodeFragment, FragmentSignCodeBinding>() {
    val enablePhone = ObservableBoolean(false)
    val enableCodeInput = ObservableBoolean(false)
    @Inject
    lateinit var api: Api
    private val timingEntity = TimingEntity()

    override fun attachView(savedInstanceState: Bundle?, t: SignCodeFragment) {
        super.attachView(savedInstanceState, t)
        bindingParams(t)
        binding.codeView.codeListener = { if (it.length == 4) loginCode(it) }
    }

    private fun bindingParams(t: SignCodeFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding.params = it }
        receiveSignEvent()
            .subscribeNormal(t, { binding.params = it.signParams })
    }

    fun onInputFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding.inputEditMobile.error = getPhoneError(s.toString())
    }

    fun onPasswordLoginClick(v: View) {
        signEvent(login, binding.params!!)
    }

    fun onCodeClick(v: View) {
        v.isEnabled = false
        binding.params?.let { params ->
            api.loginCode(params).subscribeNormal(t,{timing(v as TextView, it)},{toast(it)
                        v.isEnabled = true})
        }
    }

    private fun timing(view: TextView, it: CodeEntity) {
        timingEntity.time = 60
        timingEntity.listener = {
            view.text = String.format("%1ds", it)
            if (it == 0) {
                view.isEnabled = true
                view.text = view.context.getString(R.string.get_code)
            }
        }
        TimeUtil.add(timingEntity)
        enableCodeInput.set(!TextUtils.isEmpty(it.uid))
        binding.params?.uid = it.uid
        showInputMethod(view.context)
    }


    private fun loginCode(code: String) {
        binding.params?.let { params ->
            params.smsCode = code
            api.codeLogin(params)
                .subscribeNormal(t, {
                    loginEvent(true,it)
                    finish()
                })
        }
    }
}
