package com.customers.zktc.ui.home.page

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
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
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_page])
class HomePageModel @Inject constructor() :
    RecyclerModel<HomePageFragment, FragmentHomePageBinding, HomePageEntity<*>>() {
    val city = ObservableField<String>("定位中...")

    @Inject
    lateinit var api: Api
    private val spanCount = 2520
    override fun attachView(savedInstanceState: Bundle?, t: HomePageFragment) {
        super.attachView(savedInstanceState, t)
        val layoutManager = GridLayoutManager(t.context, spanCount)
        layoutManager.spanSizeLookup = GridSizeLookup(recyclerAdapter, spanCount)
        binding?.smartRefreshLayout?.setOnMultiListener(object : SimpleMultiListener() {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)
                onHttp(0, RecyclerStatus.loadTop)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                super.onLoadMore(refreshLayout)
                val position = layoutManager.findLastCompletelyVisibleItemPosition()
                onHttp(position,RecyclerStatus.loadBottom)
            }
        })
        layoutManagerField.set(layoutManager)
        setRxHttp { offset, refresh -> api.homePage(offset, refresh, pageCount) }
        api.locationCity(t.dataActivity).subscribeNormal (t, { city.set(it) })
    }

    override fun onNext(t: List<HomePageEntity<*>>) {
        super.onNext(t)

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