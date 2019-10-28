package com.customers.zktc.ui.home.page

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.binding.model.adapter.recycler.DiffUtilCallback
import com.binding.model.adapter.recycler.GridSizeLookup
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.RecyclerModel
import com.binding.model.inflate.obj.RecyclerStatus
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomePageBinding
import com.customers.zktc.inject.data.Api
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_page])
class HomePageModel @Inject constructor() :
    RecyclerModel<HomePageFragment, FragmentHomePageBinding, HomePageInflate<*>>() {
    val city = ObservableField<String>("定位中...")
    private val banner = HomePageBanner(arrayListOf())
    private val list = ArrayList<HomePageInflate<*>>()
    @Inject
    lateinit var api: Api
    private val spanCount = 2520
    override fun attachView(savedInstanceState: Bundle?, t: HomePageFragment) {
        super.attachView(savedInstanceState, t)
        val layoutManager = GridLayoutManager(t.context, spanCount)
        layoutManager.spanSizeLookup = GridSizeLookup(recyclerAdapter, spanCount)
        layoutManagerField.set(layoutManager)
        t.lifecycle.addObserver(banner)
        http = { offset, _ ->
            api.getRecommend(offset, pageCount)
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.goodsRecommends }
        }
        initHttp()
        binding?.recyclerView?.addItemDecoration(HomePageDecoration())
        api.locationCity(t.dataActivity).subscribeNormal(t, { city.set(it) })
        binding?.smartRefreshLayout?.setOnMultiListener(object : SimpleMultiListener() {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)
                initHttp()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                super.onLoadMore(refreshLayout)
                val position = layoutManager.findLastVisibleItemPosition()
                binding?.recyclerView?.let { onHttp(position, RecyclerStatus.loadBottom) }
            }
        })
    }

    private fun initHttp() {
        return api.homePage(1, refresh, pageCount, banner)
            .doOnSuccess { addHeadIndex(it) }
            .map { DiffUtil.calculateDiff(DiffUtilCallback(adapter.holderList, it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeNormal(t, {
                it.dispatchUpdatesTo(adapter)
                adapter.clear()
                adapter.holderList.addAll(list)
            }, onComplete = { onComplete() })
    }

    private fun addHeadIndex(it: List<HomePageInflate<*>>) {
        list.clear()
        list.addAll(it)
        var size = it.size
        for (homePageInflate in it.asReversed()) {
            if(homePageInflate is HomeGoodsRecommendEntity){
                size--
            }
        }
        headIndex = size
    }

    override fun onComplete() {
        super.onComplete()
        binding?.smartRefreshLayout?.finishRefresh()
        binding?.smartRefreshLayout?.finishLoadMore()
    }

    fun onSearchClick(v: View) {}

    fun onNoticeClick(v: View) {}

    fun onLocationClick(v: View) {}
}