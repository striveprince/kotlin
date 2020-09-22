package com.lifecycle.demo.base.anko

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
import androidx.viewpager2.widget.ViewPager2
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.ErrorInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.toast
import com.lifecycle.rx.viewmodel.list.ListViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.textView

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 14:35
 * Email: 1033144294@qq.com
 */
inline fun ViewManager.viewPager2(init: ViewPager2.() -> Unit): ViewPager2 =
    ankoView({ ViewPager2(it) }, theme = 0, init = init)
//inline fun ViewManager.bottomNavigationView(init: BottomNavigationView.() -> Unit): BottomNavigationView =
//    ankoView({ BottomNavigationView(it) }, theme = 0, init = init)
//
//inline fun ViewManager.recyclerView(init: RecyclerView.() -> Unit): RecyclerView =
//    ankoView({ RecyclerView(it) }, theme = 0, init = init)


inline fun ViewManager.smartRefreshLayout(init: SmartRefreshLayout.() -> Unit): SmartRefreshLayout =
    ankoView({ SmartRefreshLayout(it) }, theme = 0, init = init)

fun <E : Inflate> recyclerAnko(
    lifecycleInit: LifecycleInit<*>,
    t: ListViewModel<E>,
    recyclerManager: RecyclerView.LayoutManager = LinearLayoutManager(lifecycleInit.requireActivity()),
    animator: ItemAnimator? = null,
    errorInflate: ErrorInflate = errorInflate(),
    emptyString: String = "没有相关数据,点击重试"
) =
    AnkoContext.create(lifecycleInit.requireActivity()).apply {
        frameLayout {
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
                    t.errorMessage.observer(lifecycleInit.owner()) { if (it != null) errorInflate.set(onLoad,empty =  it) }
//                    t.enable.observer(lifecycleInit.owner()) { (this as View).isEnabled = it }
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
        override fun set(onLoad: (Int, Int) -> Unit, throwable: Throwable?, empty: CharSequence) {
            this.throwable = throwable
            val message = throwable?.message ?: empty
            textView.text = message
            toast(message.toString())
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
