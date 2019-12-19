package com.lifecycle.demo.base.life.recycler

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.demo.base.life.BaseFragment
import com.lifecycle.demo.base.life.viewmodel.RecyclerModel
import com.lifecycle.demo.base.util.recyclerBinding
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.exception.ApiEmptyException
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.DiffInflate
import io.reactivex.Single

@Suppress("UNCHECKED_CAST")
abstract class RecyclerFragment<E: DiffInflate> :BaseFragment<RecyclerModel<E>,ViewDataBinding>(){
    internal var loadMore = true
    override fun createView(t: RecyclerModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        val binding = parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        model.httpData = { _, position, state ->
            if(state == AdapterType.add&&!loadMore) Single.error(ApiEmptyException())
            else apiData(position, state)
        }
        super.initData(api, owner, bundle)
    }

    override fun parse(t: RecyclerModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): LayoutSwipeRecyclerViewBinding {
        return recyclerBinding(this,t)
            .apply {
                recyclerView.apply {
                    adapter = t.adapter as RecyclerView.Adapter<*>
                    layoutManager = LinearLayoutManager(context)
                    itemAnimator = null
                }
                errorView()?.let{
                    frameLayout.removeAllViews()
                    frameLayout.addView(it)
                }
            }
    }

    open fun errorView():View? = null

    abstract fun apiData(offset: Int, type: Int): Single<List<E>>

    override fun initModel(): RecyclerModel<E> {
        return ViewModelProviders.of(this)[RecyclerModel::class.java] as RecyclerModel<E>
    }
}