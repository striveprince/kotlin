package com.customers.zktc.ui.home.page

import android.content.Context
import android.renderscript.ScriptGroup
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2
import com.binding.model.App
import com.binding.model.adapter.GridInflate
import com.binding.model.adapter.IEventAdapter
import com.binding.model.adapter.recycler.RecyclerAdapter
import com.binding.model.adapter.recycler.RecyclerHolder
import com.binding.model.annoation.LayoutView
import com.binding.model.base.rotate.TimeEntity
import com.binding.model.base.rotate.TimeUtil
import com.binding.model.findModelView
import com.binding.model.inflate.ViewEntity
import com.binding.model.inflate.ViewInflate
import com.binding.model.inflate.inter.Diff
import com.binding.model.inflate.inter.Measure
import com.binding.model.inflate.inter.Recycler
import com.customers.zktc.R
import com.customers.zktc.base.arouter.ARouterUtil
import com.customers.zktc.databinding.*
import com.customers.zktc.inject.data.net.bean.*
import timber.log.Timber

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/30 11:18
 * Email: 1033144294@qq.com
 */

interface HomePageInflate<Binding : ViewDataBinding> : GridInflate<Binding>, Recycler<Binding>,
    Diff<Binding>{
    fun sorted(): Int = 0
    override fun getSpanSize() = 1
    override fun key() = getLayoutId()
    override fun value() = hashCode()
    override fun recycler(recyclerHolder: RecyclerHolder<*>) {}
}
open class HomePageEntity<Bean,Binding : ViewDataBinding>(override val bean: Bean)
    : ViewEntity<Bean,Binding>(bean), Diff<Binding>, HomePageInflate<Binding>

@LayoutView(layout = [R.layout.holder_home_page_banner])
class HomePageBanner(var operationAds: List<HomePageOperationEntity>) :
    HomePageInflate<HolderHomePageBannerBinding>, TimeEntity, LifecycleObserver,
    ViewPager2.OnPageChangeCallback() {
    @Transient override var layoutIndex = 0
    @Transient override val layoutView = findModelView(javaClass)
    @Transient val adapter = RecyclerAdapter<HomePageOperationEntity>()
    @Transient private var position = 0
    @Transient private var loopPosition = 0
    @Transient lateinit var binding:HolderHomePageBannerBinding
    override fun getSpanSize() = 1
    override fun key() = 0
    override fun value(): Int {
        var result = 0
        for (operationAd in operationAds)
            result = 31 * result + operationAd.code()
        Timber.i("result=$result")
        return result
    }

    override fun bindView(context: Context, viewGroup: ViewGroup?, binding: HolderHomePageBannerBinding) {
        this.binding = binding
        adapter.refreshListAdapter(0, operationAds)
        TimeUtil.add(this)
        binding.viewPager2.registerOnPageChangeCallback(this)
    }

    override fun getTurn() {
        if (++loopPosition % 4 == 0 && operationAds.isNotEmpty()) {
            position = if (position == operationAds.size) 0 else position + 1
            binding.viewPager2.currentItem = position
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        TimeUtil.remove(this)
        binding.viewPager2.unregisterOnPageChangeCallback(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
        TimeUtil.switching(this, state)
    }
}


@LayoutView(layout = [R.layout.holder_home_page_banner_item, R.layout.holder_home_page_store])
class HomePageOperationEntity(bean: HomePageOperationBean)
    : HomePageEntity<HomePageOperationBean,ViewDataBinding>(bean), Measure {
    override fun getSpanSize() = 1
    fun getRadius() = 5
    fun onRouteClick(v: View) {
        ARouterUtil.homeNavigation(bean.linkUrl, bean.name)
    }

    override fun measure(view: View, parent: ViewGroup?): ViewGroup.LayoutParams {
        return when (layoutIndex) {
            0 -> {
                val params = view.layoutParams
                params.width = (App.getScreenWidth() - App.floatToPx(30f).toInt()) / 2
                params.height = params.width
                params
            }
            else -> view.layoutParams
        }
    }

    fun code(): Int {
        var result = bean.id
        result = result*31+bean.linkUrl.hashCode()
        result = result*31+bean.name.hashCode()
        result = result*31+bean.picture.hashCode()
        result = result*31+bean.viceName.hashCode()
        return result
    }
}


@LayoutView(layout = [R.layout.holder_home_page_category])
class HomeCategoryEntity(
    bean:HomeCategoryBean
) : HomePageEntity<HomeCategoryBean,HolderHomePageCategoryBinding>(bean) {
    override fun getSpanSize() = 5
    override fun sorted() = 20
    fun onRouteClick(v: View) {
        ARouterUtil.homeNavigation(bean.linkUrl, bean.name)
    }
}

@LayoutView(layout = [R.layout.holder_home_page_floor_title])
class HomeFloorDataEntity(bean: HomeFloorDataBean) :
    HomePageEntity<HomeFloorDataBean,HolderHomePageFloorTitleBinding>(bean) {
    override fun getSpanSize() = 1
    override fun sorted() = 30
}

@LayoutView(layout = [R.layout.holder_home_page_area])
class HomePageAreaData(val operationAds: List<HomePageOperationEntity>) :ViewInflate<HolderHomePageAreaBinding>(),
    HomePageInflate<HolderHomePageAreaBinding>{
    override fun getSpanSize() = 1
    override fun sorted() = 40
}


@LayoutView(layout = [R.layout.holder_home_page_floor, R.layout.holder_home_page_floor2])
class HomeFloorTypeEntity(
bean:HomeFloorTypeBean
) : HomePageEntity<HomeFloorTypeBean,ViewDataBinding>(bean) {
    override fun getSpanSize() = layoutIndex+1
    override fun sorted() = 30
    fun getRadius() = 5

    fun onRouteClick(v: View) {
        ARouterUtil.homeNavigation(bean.floorLinkUrl, bean.floorName)
    }
}

@LayoutView(layout = [R.layout.holder_home_page_mall])
data class HomeMallEntity(val list: List<HomePageOperationEntity>) :
    HomePageInflate<HolderHomePageMallBinding>,ViewInflate<HolderHomePageMallBinding>() {
    override fun sorted() = 60
}

@LayoutView(layout=[R.layout.holder_home_page_store_recommend_title])
class StoreInfoEntity(
    bean: StoreInfoBean,
    val recommendGoodsList: List<HomeGoodsRecommendListEntity>
):HomePageEntity<StoreInfoBean,ViewDataBinding>(bean){
    override fun sorted() = 61
}

@LayoutView(layout = [R.layout.holder_home_page_store_recommend])
class HomeGoodsRecommendListEntity(recommendListBean: HomeGoodsRecommendListBean):
    HomePageEntity<HomeGoodsRecommendListBean,HolderHomePageStoreRecommendBinding>(recommendListBean){
    fun getRadius()=5
    fun onRouteClick(v:View){
    }
}




@LayoutView(layout = [R.layout.holder_home_page_marketing_title, R.layout.holder_home_page_recommend_title])
class HomePageTitle(layoutIndex: Int=0) : HomePageInflate<ViewDataBinding>,ViewInflate<ViewDataBinding>() {
    init { this.layoutIndex = layoutIndex }
    override fun getSpanSize() = 1
    override fun sorted() = when (layoutIndex) {
        0 -> 60
        else -> 80 }
    fun onMoreClick(view: View) {}
}

@LayoutView(layout = [R.layout.holder_home_page_market, R.layout.holder_home_page_assemble_item])
class HomeGoodVosEntity(
    bean:HomeGoodVosBean
) : HomePageEntity<HomeGoodVosBean,ViewDataBinding>(bean) {
    override fun getSpanSize() = 1
    override fun sorted() = 60
    fun getPrice(): CharSequence {
        return "￥15"
    }

    fun getAssembled(): String {
        return "已拼${bean.marketingVo.groupedNumbers}件"
    }

    fun onAssembleClick(v: View) {

    }
}

@LayoutView(layout = [R.layout.holder_home_page_assemble])
data class HomeAssembleEntity(val goodsVos: List<HomeGoodVosEntity>) :
    HomePageInflate<HolderHomePageAssembleBinding>,ViewInflate<HolderHomePageAssembleBinding>() {
    init { for (goodsVo in goodsVos) goodsVo.layoutIndex = 1 }
    override fun sorted() = 70
    override fun bindView(context: Context, viewGroup: ViewGroup?, binding: HolderHomePageAssembleBinding) {
        val adapter = RecyclerAdapter<HomeGoodVosEntity>()
        adapter.refreshListAdapter(0, goodsVos)
        binding.viewPager.adapter = adapter
    }
}


@LayoutView(layout = [R.layout.holder_home_page_recommend])
class HomeGoodsRecommendEntity(
   bean :HomeGoodsRecommendBean
) : HomePageEntity<HomeGoodsRecommendBean,HolderHomePageRecommendBinding>(bean) {
    override fun getSpanSize() = 2
    override fun sorted(): Int = 80
    override fun bindView(context: Context, viewGroup: ViewGroup?, binding: HolderHomePageRecommendBinding) {
        super.bindView(context, viewGroup, binding)
        val width = App.getScreenWidth()/2-App.toPx(15)
        val layoutParams = binding.imageView.layoutParams
        layoutParams.width = width
        layoutParams.height = width
        binding.imageView.layoutParams = layoutParams
    }

    fun getPrice():String{
        return bean.preferPrice.toString()
    }
}
