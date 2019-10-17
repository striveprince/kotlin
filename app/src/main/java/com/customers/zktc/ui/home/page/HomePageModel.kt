package com.customers.zktc.ui.home.page

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.binding.model.adapter.GridInflate
import com.binding.model.adapter.recycler.GridSizeLookup
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.RecyclerModel
import com.binding.model.inflate.obj.RecyclerEvent
import com.binding.model.inflate.obj.RecyclerStatus
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomePageBinding
import com.customers.zktc.inject.data.Api
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnMultiListener
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_page])
class HomePageModel @Inject constructor() :
    RecyclerModel<HomePageFragment, FragmentHomePageBinding, HomeBaseInflate<*>>() {
    val city = ObservableField<String>("定位中...")

    @Inject
    lateinit var api: Api
    private val spanCount = 12
    override fun attachView(savedInstanceState: Bundle?, t: HomePageFragment) {
        super.attachView(savedInstanceState, t)
        binding?.smartRefreshLayout?.setOnMultiListener(object : SimpleMultiListener() {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)
                onHttp(0, RecyclerStatus.loadTop)
                binding?.smartRefreshLayout?.finishRefresh()
            }
        })
        val layoutManager = GridLayoutManager(t.context, spanCount)
        layoutManager.spanSizeLookup = GridSizeLookup(recyclerAdapter, spanCount)
        layoutManagerField.set(layoutManager)
        setRxHttp { offset, refresh -> api.homePage(offset, refresh, pageCount) }
        api.locationCity(t.dataActivity).subscribeNormal (t, { city.set(it) })
    }

    fun onSearchClick(v: View) {}

    fun onNoticeClick(v: View) {}

    fun onLocationClick(v: View) {}


}