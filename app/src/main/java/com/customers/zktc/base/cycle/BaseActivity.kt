package com.customers.zktc.base.cycle

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.binding.model.base.container.DataBindingActivity
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.inject.component.DaggerActivityComponent
import com.customers.zktc.inject.module.ActivityModule
import com.customers.zktc.ui.ZktcApplication
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel<*, *>> : DataBindingActivity<ActivityComponent>() {

    @Inject lateinit var vm: VM

    override fun inject(savedInstanceState: Bundle?, parent: ViewGroup?, attachToParent: Boolean): View {
        val method = ActivityComponent::class.java.getDeclaredMethod("inject",this::class.java)
        method.invoke(component,this)
        return vm.attachContainer(this, parent, attachToParent, savedInstanceState).root
    }

    override val component: ActivityComponent
        get() =  DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .appComponent(ZktcApplication.component).build()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(vm.onSaveInstanceState(outState))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        vm.onRestoreInstanceState(savedInstanceState)
    }
}