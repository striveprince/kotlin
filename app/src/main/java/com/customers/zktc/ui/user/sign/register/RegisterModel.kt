package com.customers.zktc.ui.user.sign.register

import android.os.Bundle
import android.text.Editable
import androidx.databinding.ObservableBoolean
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.rxBus
import com.customers.zktc.R
import com.customers.zktc.base.util.getPhoneError
import com.customers.zktc.databinding.FragmentRegisterBinding
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.user.sign.SignEvent
import com.customers.zktc.ui.user.sign.SignParams
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
    val enablePassword = ObservableBoolean(false)
    override fun attachView(savedInstanceState: Bundle?, t: RegisterFragment) {
        super.attachView(savedInstanceState, t)
        bindingParams(t)
    }

    private fun bindingParams(t: RegisterFragment) {
        t.arguments?.getParcelable<SignParams>(Constant.params)?.let { binding?.params = it }
        addDisposables(rxBus<SignEvent>(t).subscribe({
            binding?.params = it.signParams
        }, { it.printStackTrace() }))
    }

    fun onPhoneFinish(s: Editable) {
        enablePhone.set(getPhoneError(s.toString()) == null)
        binding?.inputEditMobile?.error = getPhoneError(s.toString())
    }

    fun onPasswordFinish(s: Editable) {
        enablePassword.set(true)
    }

}