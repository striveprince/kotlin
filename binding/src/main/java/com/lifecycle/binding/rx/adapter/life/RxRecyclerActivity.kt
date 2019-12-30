package com.lifecycle.binding.rx.adapter.life

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.BaseActivity
import com.lifecycle.binding.util.recyclerBinding
import com.lifecycle.binding.rx.viewmodel.list.RxListViewModel
import io.reactivex.Single


abstract class RxRecyclerActivity<E : Diff> : BaseActivity<RxListViewModel<E>,LayoutSwipeRecyclerViewBinding>() {

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.httpData = {offset,state-> apiData(offset, state) }
    }

    abstract fun apiData(offset:Int,state:Int): Single<List<E>>
    override fun createView(t: RxListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        val binding = parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun parse(t: RxListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): LayoutSwipeRecyclerViewBinding {
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

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): RxListViewModel<E> {
        return ViewModelProviders.of(this)[RxListViewModel::class.java] as RxListViewModel<E>
    }
}