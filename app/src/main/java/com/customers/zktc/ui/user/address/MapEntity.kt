package com.customers.zktc.ui.user.address

import com.amap.api.services.core.PoiItem
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewEntity
import com.customers.zktc.R
import com.customers.zktc.base.adapter.Filter
import com.customers.zktc.databinding.HolderMapTextBinding

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 11:19
 * Email: 1033144294@qq.com
 */
@LayoutView(layout=[R.layout.holder_map_text])
class MapEntity(
    address: PoiItem,
    private val province:String,
    private val city:String,
    private val district:String
):ViewEntity<PoiItem,HolderMapTextBinding>(address),Filter<HolderMapTextBinding>{
    override fun compare(text: String)=getAddress().contains(text)

    fun getAddress():String{
        return "$province$city$district${bean.title}"
    }

    override fun toString()=getAddress()
}