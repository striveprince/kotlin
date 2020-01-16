package com.lifecycle.binding.life

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.life.AppLifecycle.Companion.appLifecycle

import com.lifecycle.binding.inter.Parse
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<Model:ViewModel,B>:Fragment(),Parse<Model,B>, LifecycleInit<Model> {
    val model: Model by lazy { initModel() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inject(savedInstanceState).apply { initData(this@BaseFragment,savedInstanceState) }
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        model.let { if(it is Init)it.initData(this,bundle) }
    }

    override fun inject(savedInstanceState: Bundle?)= createView(model, activity!!)

    override fun initModel():Model {
        val clazz = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<Model>
        return ViewModelProviders.of(this)[clazz]
    }

    override fun t() = model
    override fun owner()=this
//    override fun fragmentManager()=childFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appLifecycle.onCreate(this,savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        appLifecycle.onResume(this)
    }

    override fun onStart() {
        super.onStart()
        appLifecycle.onStart(this)
    }

    override fun onPause() {
        super.onPause()
        appLifecycle.onPause(this)
    }

    override fun onStop() {
        super.onStop()
        appLifecycle.onStop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        appLifecycle.onDestroy(this)
    }
}