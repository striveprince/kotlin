package com.lifecycle.coroutines.adapter.life

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.BaseFragment
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.coroutines.inflate.RecyclerParse
import com.lifecycle.coroutines.viewmodel.list.HttpData
import com.lifecycle.coroutines.viewmodel.list.ListViewModel


abstract class RecyclerFragment<E : Diff> : BaseFragment<ListViewModel<E>, LayoutSwipeRecyclerViewBinding>(),
    HttpData<List<E>> {

    override fun createView(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        val binding = parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.http = this
    }

    override fun parse(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): LayoutSwipeRecyclerViewBinding {
        return RecyclerParse(t).parse(t, requireActivity(), parent, false).apply {
            recyclerView.apply {
                adapter = t.adapter as RecyclerView.Adapter<*>
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
            }
            tipView(t.error)?.let {
                frameLayout.removeAllViews()
                frameLayout.addView(it)
            }
        }
    }

    open fun tipView(s: MutableLiveData<Throwable>): View? = null

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListViewModel<E> {
        return ViewModelProvider(this)[ListViewModel::class.java] as ListViewModel<E>
    }
}