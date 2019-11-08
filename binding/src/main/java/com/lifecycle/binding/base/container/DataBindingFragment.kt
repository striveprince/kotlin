package com.lifecycle.binding.base.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.binding.inflate.inter.Inflate

class DataBindingFragment <Binding:ViewDataBinding>: Fragment(),Inflate<Binding>{
    lateinit var binding: Binding
    override fun binding()= binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this)
        binding = attachContainer(context!!,container,false,savedInstanceState)
        binding.lifecycleOwner = this
        return  binding.root
    }

}
