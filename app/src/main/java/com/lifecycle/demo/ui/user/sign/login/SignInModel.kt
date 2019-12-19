package com.lifecycle.demo.ui.user.sign.login

import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lifecycle.demo.base.life.viewmodel.BaseViewModel
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.base.util.getAccountError
import com.lifecycle.demo.base.util.getPasswordError
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.SignParams
import com.lifecycle.demo.inject.data.net.bean.TokenBean
import com.lifecycle.binding.util.subscribeObserver
import io.reactivex.Single


class SignInModel:BaseViewModel() {
    val enableAccount = MutableLiveData(true)
    val enablePassword = MutableLiveData(true)
    val sign by lazy { api.preferenceApi.signParams() }

    fun onLoginClick(v: View){
        api.signIn(sign)
            .doSubscribe(v)
            .subscribeObserver{ ARouterUtil.home() }
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
