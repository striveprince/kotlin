package com.customers.zktc.ui.home.page

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.recycler.RecyclerAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewGridInflate
import com.customers.zktc.R
import com.customers.zktc.databinding.LayoutBannerBinding

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 16:12
 * Email: 1033144294@qq.com
 */

@LayoutView(layout=[R.layout.layout_banner])
data class HomePageBannerData(val operationAds:List<HomePageBannerEntity>): ViewGridInflate<ViewDataBinding>(){
    @Transient val adapter= RecyclerAdapter<HomePageBannerEntity>()

    override fun getSpanSize() = 1

    override fun bindView(context: Context, binding: ViewDataBinding) {
        super.bindView(context, binding)
        if(binding is LayoutBannerBinding)
            adapter.addListAdapter(0,operationAds)
    }

}