package com.lifecycle.demo.ui.user.sign.login

import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.base.util.api
import com.lifecycle.demo.base.util.getAccountError
import com.lifecycle.demo.base.util.getPasswordError
import com.lifecycle.rx.util.subscribeObserver
import com.lifecycle.rx.viewmodel.LifeViewModel


class SignInModel: LifeViewModel() {
    val enableAccount = MutableLiveData(true)
    val enablePassword = MutableLiveData(true)
    val sign by lazy { api.preferenceApi.signParams() }

    fun onLoginClick(v: View){
        api.signIn(sign)
            .doSubscribe(v)
            .subscribeObserver(onError = {ARouterUtil.home()}){ ARouterUtil.home() }
    }

    fun checkAccountError(editable: Editable?):CharSequence?{
        getAccountError(editable.toString()).let {
            enableAccount.value = it == null
            return it
        }
    }

    fun checkPasswordError(editable: Editable?):CharSequence?{
        getPasswordError(editable.toString()).let {
            enablePassword.value = it == null
            return it
        }
    }
}
