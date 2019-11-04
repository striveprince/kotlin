package com.customers.zktc.ui.home.cart

import androidx.databinding.ViewDataBinding
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewEntity
import com.binding.model.toEntities
import com.customers.zktc.R
import com.customers.zktc.inject.data.net.bean.HomeCartGoodsBean
import com.customers.zktc.inject.data.net.bean.HomeCartStoreBean

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/1 9:22
 * Email: 1033144294@qq.com
 */

@LayoutView(layout=[R.layout.holder_home_cart_store])
class HomeCartStoreEntity(homeCartStoreBean: HomeCartStoreBean):
    ViewEntity<HomeCartStoreBean,ViewDataBinding>(homeCartStoreBean){

    val entities = bean.shoppingCartList.toEntities<HomeCartGoodsEntity>()
}

@LayoutView(layout=[R.layout.holder_home_cart_goods])
class HomeCartGoodsEntity(bean: HomeCartGoodsBean):
    ViewEntity<HomeCartGoodsBean,ViewDataBinding>(bean){

}