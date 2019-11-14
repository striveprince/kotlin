package com.binding.model.inflate.inter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.binding.model.Config

interface Entity<Bean,Binding:ViewDataBinding>:Inflate<Binding>{
    val bean:Bean
    override fun bindView(context: Context, viewGroup: ViewGroup?, binding: Binding) {
        super.bindView(context, viewGroup, binding)
        binding.setVariable(Config.bean,bean)
    }
}