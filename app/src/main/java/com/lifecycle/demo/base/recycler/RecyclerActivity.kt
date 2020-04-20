package com.lifecycle.demo.base.recycler

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.BaseActivity
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding
import kotlinx.coroutines.flow.flow


abstract class RecyclerActivity<E : Diff> : BaseActivity<ListViewModel<E>, LayoutSwipeRecyclerViewBinding>(){

    lateinit var binding: LayoutSwipeRecyclerViewBinding
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.httpData = {_,_-> flow { emit(ArrayList<E>()) }}
    }

    override fun createView(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        binding = parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun parse(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): LayoutSwipeRecyclerViewBinding {
        return RecyclerParse(t).parse(t, requireActivity(), parent, false).apply {
            recyclerView.apply {
                adapter = t.adapter as RecyclerView.Adapter<*>
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
            }
            tipView(t)?.let {
                frameLayout.removeAllViews()
                frameLayout.addView(it)
            }
        }
    }

    open fun tipView(s: ListViewModel<E>): View? = null

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListViewModel<E> {
        return ViewModelProvider(this)[ListViewModel::class.java] as ListViewModel<E>
    }
}