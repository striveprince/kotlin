package com.lifecycle.binding.life

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.App

import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.viewmodel.LifeViewModel
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<Model:ViewModel,B>:Fragment(),Parse<Model,B>, LifecycleInit<Model> {
    lateinit var model: Model
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        App.startCreate(this)
        initData(this,savedInstanceState)
        return inject(savedInstanceState)
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        model.let { if(it is LifeViewModel)it.attachData(this,bundle) }
    }

    override fun inject(savedInstanceState: Bundle?)= createView(model, activity!!)

    override fun initModel():Model {
        val clazz = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<Model>
        return ViewModelProviders.of(this)[clazz]
    }

    override fun t() = model
    override fun owner()=this


}