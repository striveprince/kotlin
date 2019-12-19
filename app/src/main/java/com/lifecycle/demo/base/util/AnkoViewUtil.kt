package com.lifecycle.demo.base.util

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.viewmodel.RecyclerModel
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.demo.base.life.LifecycleInit
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.inter.inflate.ErrorInflate
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView

inline fun ViewManager.smartRefreshLayout(init: SmartRefreshLayout.() -> Unit): SmartRefreshLayout =
    ankoView({ SmartRefreshLayout(it) }, theme = 0, init = init)

fun <E : DiffInflate> recyclerAnko(
    lifecycleInit: LifecycleInit<*, *, *>,
    t: RecyclerModel<E>,
    recyclerManager: RecyclerView.LayoutManager = LinearLayoutManager(lifecycleInit.getActivity()),
    animator: ItemAnimator? = null,
    errorInflate: ErrorInflate = errorInflate(),
    emptyString: String = "没有相关数据,点击重试"
) =
    AnkoContext.create(lifecycleInit.getActivity()!!).apply {
        frameLayout {
            backgroundColorResource = R.color.recycler_back_ground
            lparams(matchParent, matchParent)
            val errorView = errorInflate.createView(context, this)
            smartRefreshLayout {
                frameLayout {
                    recyclerView {
                        lparams(matchParent, matchParent)
                        adapter = t.adapter as RecyclerAdapter
                        layoutManager = LinearLayoutManager(context)
                        itemAnimator = animator
                        lparams(matchParent, matchParent)
                    }
                    errorView.visibility = GONE
                    addView(errorView, matchParent, matchParent)
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, matchParent)
                .apply {
                    val onLoad: (Int, Int) -> Unit = { _, type ->
                        if (t.loadingState.value == 0) {
                            t.loadingState.value = type
                        }
                    }
                    t.error.observer(lifecycleInit.owner()) { if (it != null) errorInflate.set(onLoad, it) }
                    t.enable.observer(lifecycleInit.owner()) { (this as View).isEnabled = it }
                    t.loadingState.observer(lifecycleInit.owner()) {
                        when (it) {
                            0 -> {
                                errorView.visibility = if (t.adapterList.isEmpty()) VISIBLE else GONE
                                if (t.size() == 0) errorInflate.set(onLoad, empty = emptyString)
                                finishLoadMore()
                                finishRefresh()
                            }
                            AdapterType.refresh -> autoRefresh()
                            AdapterType.add -> autoLoadMore()
                        }
                    }
                    setOnMultiListener(object : SimpleMultiListener() {
                        override fun onLoadMore(refreshLayout: RefreshLayout) {
                            super.onLoadMore(refreshLayout)
                            if (recyclerManager is LinearLayoutManager) {
                                val position = recyclerManager.findLastVisibleItemPosition()
                                onLoad(position, AdapterType.add)
                            }
                        }

                        override fun onRefresh(refreshLayout: RefreshLayout) {
                            super.onRefresh(refreshLayout)
                            onLoad(0, AdapterType.refresh)
                        }
                    })
                }
        }.layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
    }


private fun errorInflate(): ErrorInflate {
    return object : ErrorInflate {
        lateinit var textView: TextView
        var throwable: Throwable? = null

        var view: View? = null
        override fun set(onLoad: (Int, Int) -> Unit, throwable: Throwable?, empty: String) {
            this.throwable = throwable
            val message = throwable?.message ?: empty
            textView.text = message
            toast(message)
            textView.setOnClickListener { onLoad(0, AdapterType.refresh) }
        }

        override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
            if (view == null)
                view = AnkoContext.create(context).apply {
                    frameLayout {
                        textView = textView(throwable?.message ?: "") {
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, matchParent, gravity = Gravity.CENTER)
                        lparams(matchParent, matchParent, gravity = Gravity.CENTER)
                    }
                }.view
            return view!!
        }
    }
}
