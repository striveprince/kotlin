package com.lifecycle.demo.ui.user.sign.login

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.databinding.ActivitySignInBinding
import com.lifecycle.demo.inject.component.ActivityComponent.Config.tomtaw
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.ui.user.sign.login.SignInActivity.Companion.signIn
import com.lifecycle.binding.inter.bind.annotation.LayoutView

@Route(path = signIn)
@LayoutView(layout = [R.layout.activity_sign_in])
class SignInActivity: DataBindingActivity<SignInModel, ActivitySignInBinding>() {
    companion object{ const val signIn = tomtaw+"signIn"}

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        binding.params = model.sign
    }

}