package com.customers.zktc.base.cycle

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.binding.model.base.container.DataBindingFragment
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.inject.component.DaggerFragmentComponent
import com.customers.zktc.inject.component.FragmentComponent
import com.customers.zktc.inject.module.FragmentModule
import com.customers.zktc.ui.ZktcApplication
import javax.inject.Inject


abstract class BaseFragment<VM : ViewModel<*, *>> : DataBindingFragment<FragmentComponent>() {
    @Inject
    lateinit var vm: VM

    override fun inject(
        savedInstanceState: Bundle?,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): View {
        val func = FragmentComponent::class.java.getDeclaredMethod("inject", this::class.java)
        func.invoke(component, this)
        return vm.attachContainer(this, parent, attachToParent, savedInstanceState).root
    }

    override val component: FragmentComponent
        get() = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule(this))
            .appComponent(ZktcApplication.component).build()

    override fun finish() {
        dataActivity.finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(vm.onSaveInstanceState(outState))
    }

}
