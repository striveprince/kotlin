package com.lifecycle.demo.base.life

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter

import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.FragmentModule
import com.lifecycle.demo.ui.DemoApplication
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.inter.Parse
import javax.inject.Inject
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<Model:ViewModel,B>:Fragment(),Parse<Model,B>, LifecycleInit<Model, FragmentComponent.Builder, Api> {
    lateinit var model: Model
    @Inject lateinit var api: Api
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inject(DemoApplication.application.fragmentBuilder,savedInstanceState)
        initView(savedInstanceState)
        return view
    }

    override fun initView(savedInstanceState: Bundle?) {}

    @Suppress("UNCHECKED_CAST")
    override fun inject(builder: FragmentComponent.Builder, savedInstanceState: Bundle?):View {
        ARouter.getInstance().inject(this)
        model = initModel()
        val component = builder.applyFragmentModule(FragmentModule(this)).build()
        val method = FragmentComponent::class.java.getDeclaredMethod("inject",this::class.java)
        method.invoke(component,this)
        val view = createView(model, activity!!)
        initData(api,this,savedInstanceState)
        return view
    }

    override fun initModel():Model {
        val clazz = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<Model>
        return ViewModelProviders.of(this)[clazz]
    }

    @CallSuper
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        if(model is Init<*>)(model as Init<Api>).initData(api,owner,bundle)
    }

    override fun t() = model
    override fun owner()=this


}