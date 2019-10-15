package com.customers.zktc.ui.user.sign.register

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.base.rotate.TimeUtil
import com.binding.model.base.rotate.TimingEntity
import com.binding.model.base.spannable.SpannableUtil
import com.binding.model.busPost
import com.binding.model.inflate.model.ViewModel
import com.binding.model.rxBus
import com.binding.model.subscribeApi
import com.binding.model.toast
import com.customers.zktc.R
import com.customers.zktc.base.util.getCodeError
import com.customers.zktc.base.util.getPasswordError
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentRegisterBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignEvent
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.user.sign.login.LoginFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 15:20
 * Email: 1033144294@qq.com
 */
@LayoutView(layout = [R.layout.fragment_register])
class RegisterModel @Inject constructor() : ViewModel<RegisterFragment, FragmentRegisterBinding>() {
    val enablePhone = ObservableBoolean(false)
    val enableCodeInput = ObservableBoolean(false)
    val enableCode = ObservableBoolean(false)
    val enablePassword = ObservableBoolean(false)
    val enableAgreement = ObservableBoolean(true)
    val enableConfirm = ObservableBoolean(false)
    @Inject
    lateinit var api: Api
    private val timingEntity = TimingEntity()

    override fun attachView(savedInstanceState: Bundle?, t: RegisterFragment) {
        super.attachView(savedInstanceState, t)
        t.lifecycle.addObserver(timingEntity)
        bindingParams(t)
        val color = ContextCompat.getColor(t.dataActivity, R.color.z_ee2d40)
        binding?.checkbox?.let {
            SpannableUtil(it)
                .addText("我已阅读并同意《")
                .addClick("用户协议", "", { showAgreement() }, { ds ->
                    ds.color = color
                    ds.isUnderlineText = true
                })
                .addText("》")
                .build()
        }
    }

    fun onRegisterClick(v: View) {
        api.register(binding!!.params!!)
            .subscribeApi(t) {

            }
    }

    fun onCodeClick(v: View) {
        binding?.inputEditCode?.findFocus()
        v.isEnabled = false
        api.code(binding!!.params!!.mobile).subscribeApi(t, { timing(v as TextView, it) }, {
            v.isEnabled = true
            toast(it)
        })

    }

    private fun timing(view: TextView, it: CodeEntity) {
        timingEntity.time = 60
        timingEntity.listener = { view.text = String.format("%1ds", it) }
        TimeUtil.add(timingEntity)
        enableCodeInput.set(!TextUtils.isEmpty(it.uid))
        binding?.params?.uid = it.uid
    }

    private fun showAgreement() {
        Timber.i("showAgreement")
    }

    private fun bindingParams(t: RegisterFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding?.params = it }
        rxBus<SignEvent>(t).subscribeApi(t) { binding?.params = it.signParams }
    }


    fun onCodeFinish(s: Editable) {
        enableCode.set(getCodeError(s.toString()) == null)
        binding?.inputEditCode?.error = getCodeError(s.toString())
    }

    fun onPhoneFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }

    fun onPasswordFinish(s: Editable) {
        enablePassword.set(getPasswordError(s.toString()) == null)
        binding?.inputPassword?.error = getPasswordError(s.toString())
    }


    /**
     *
     * confirm password compare
     * */
    fun onPasswordConfirmFinish(s: Editable) {
        binding?.params?.let {
            if (it.password != it.confirmPassword)
                binding?.inputConfirmPassword?.error = "两次输入的密码不一致"
            enableConfirm.set(true)
        }
    }

    fun onInviteCodeFinish(s: Editable) {

    }

    /**
     * password login
     * */
    fun onPasswordLoginClick(v: View) {
        binding?.params?.let { busPost(SignEvent(LoginFragment.login, it)) }
    }
}