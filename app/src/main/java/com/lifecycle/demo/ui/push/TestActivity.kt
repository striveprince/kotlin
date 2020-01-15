package com.lifecycle.demo.ui.push

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.viewmodel.LifeViewModel
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.demo.ui.push.TestActivity.Companion.test
import timber.log.Timber

@Route(path = test)
@LayoutView(layout = [R.layout.activity_test])
class TestActivity : DataBindingActivity<LifeViewModel, ViewDataBinding>() {
    companion object {
        const val test = ActivityComponent.Config.tomtaw + "test"
    }

    val position = MutableLiveData(1)

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        position.observer(owner) { Timber.i(it.toString()) }
    }
}
