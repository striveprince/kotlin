package com.customers.zktc.ui.home.page

import android.content.Context
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
import com.binding.model.inflate.ViewParse
import com.binding.model.inflate.inter.Diff
import com.binding.model.inflate.inter.Measure
import com.binding.model.inflate.inter.Recycler
import com.customers.zktc.R
import com.customers.zktc.base.arouter.ARouterUtil
import com.customers.zktc.databinding.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import timber.log.Timber

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:15
 * Email: 1033144294@qq.com
 */
interface HomePageInflate<Binding : ViewDataBinding> : GridInflate<Binding>, Recycler<Binding>,
    Diff<Binding> {
    fun sorted(): Int = 0
    override fun getSpanSize() = 1
    override fun key() = getLayoutId()
    override fun value() = hashCode()
    override fun recycler(recyclerHolder: RecyclerHolder<*>) {}
}

open class HomePageEntity<Binding : ViewDataBinding> : ViewParse<Binding>(), Diff<Binding>,
    HomePageInflate<Binding>

@Serializable
data class HomePageOperationData(val operationAds: List<HomePageOperationEntity>)

//banner图界面
@LayoutView(layout = [R.layout.layout_home_banner])
class HomePageBanner(var operationAds: List<HomePageOperationEntity>) :
    HomePageInflate<LayoutHomeBannerBinding>, TimeEntity, LifecycleObserver,
    ViewPager2.OnPageChangeCallback() {
    @Transient override var iEventAdapter: IEventAdapter<*>? = null
    @Transient override var layoutIndex = 0
    @Transient override val layoutView = findModelView(javaClass)
    @Transient val adapter = RecyclerAdapter<HomePageOperationEntity>()
    @Transient private var position = 0
    @Transient private var loopPosition = 0
    @Transient override var binding: LayoutHomeBannerBinding? = null

    override fun getSpanSize() = 1
    override fun key() = 0
    override fun value(): Int {
        var result = 0
        for (operationAd in operationAds) {
            result = 31 * result + operationAd.id
            result = 31 * result + operationAd.name.hashCode()
            result = 31 * result + operationAd.picture.hashCode()
            result = 31 * result + operationAd.linkUrl.hashCode()
        }
        Timber.i("result=$result")
        return result
    }

    override fun bindView(
        context: Context,
        viewGroup: ViewGroup?,
        binding: LayoutHomeBannerBinding
    ) {
        adapter.refreshListAdapter(0, operationAds)
        TimeUtil.add(this)
        binding.viewPager2.registerOnPageChangeCallback(this)

    }

    override fun getTurn() {
        if (++loopPosition % 4 == 0 && operationAds.isNotEmpty()) {
            position = if (position == operationAds.size) 0 else position + 1
            binding?.viewPager2?.currentItem = position
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        TimeUtil.remove(this)
        binding?.viewPager2?.unregisterOnPageChangeCallback(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
        TimeUtil.switching(this, state)
    }
}

@LayoutView(layout = [R.layout.layout_home_banner_item, R.layout.layout_home_mall_item])
@Serializable
data class HomePageOperationEntity(
    val id: Int = 0,
    val name: String = "",
    val picture: String = "",
    val viceName: String = "",
    val orderBy: Int = 0,
    val linkUrl: String = ""
) : HomePageEntity<LayoutHomeBannerItemBinding>(), Measure {
    override fun measure(view: View, parent: ViewGroup?): ViewGroup.LayoutParams {
        return when (layoutIndex) {
            0 -> {
                val params = view.layoutParams
                params.width = (App.getScreenWidth() - App.floatToPx(30f).toInt()) / 2
                params.height = params.width
                params }
            else -> view.layoutParams
        }
    }
    override fun getSpanSize() = 1
    fun getRadius() = 5
    fun onRouteClick(v: View) { ARouterUtil.homeNavigation(linkUrl, name) }
}

//分类
@Serializable
data class HomeCategoryData(val operationHomeCategorys: List<HomeCategoryEntity>)

//分类
@LayoutView(layout = [R.layout.layout_home_category])
@Serializable
data class HomeCategoryEntity(
    val iconUrl: String,
    val linkUrl: String,
    val name: String,
    val orderBy: Int
) : HomePageEntity<LayoutHomeCategoryBinding>() {
    override fun getSpanSize() = 5
    override fun sorted() = 2
    fun onRouteClick(v: View) {
        ARouterUtil.homeNavigation(linkUrl, name)
    }
}

@Serializable
data class HomeFloorData(
    val operationFloorTypes: List<HomeFloorDataEntity>
)

@Serializable
@LayoutView(layout = [R.layout.layout_home_floor_title])
data class HomeFloorDataEntity(
    val name: String,
    val pictureNumber: Int = 1,
    val operationFloors: List<HomeFloorTypeEntity>
) : HomePageEntity<LayoutHomeFloorTitleBinding>() {
    override fun getSpanSize() = 1
    override fun sorted() = 3
}

//@LayoutView(layout = [R.layout.layout_home_floor,R.layout.layout_home_floor2])
@LayoutView(layout = [R.layout.layout_home_floor])
@Serializable
data class HomeFloorTypeEntity(
    val floorLinkUrl: String,
    val floorName: String,
    val floorPicture: String,
    val floorViceName: String,
    val id: Int,
    val orderBy: Int,
    var pictureNumber: Int = 1//should add default value when json didn't have this key
) : HomePageEntity<LayoutHomeFloorBinding>() {
    override fun getSpanSize() = pictureNumber
    override fun sorted() = 3
    fun getRadius() = 5

    fun onRouteClick(v: View) {
        ARouterUtil.homeNavigation(floorLinkUrl, floorName)
    }
}

//活动专区界面
@LayoutView(layout = [R.layout.layout_home_area])
class HomePageAreaData(val operationAds: List<HomePageOperationEntity>) :
    HomePageEntity<LayoutHomeAreaBinding>() {
    override fun getSpanSize() = 1
    override fun sorted() = 4
}

@LayoutView(layout = [R.layout.layout_home_mall])
data class HomeMallEntity(val list: List<HomePageOperationEntity>) :
    HomePageEntity<LayoutHomeMallBinding>() {
    override fun sorted() = 5
}

@Serializable
data class HomeGoodsVoData(
    val goodsVos: List<HomeGoodVosEntity>
)


@LayoutView(layout = [R.layout.layout_home_rush_title])
data class HomeRushTitle(val name: String) : HomePageEntity<LayoutHomeRushTitleBinding>() {
    override fun getSpanSize() = 1
    override fun sorted() = 7
}

@Serializable
@LayoutView(layout = [R.layout.layout_home_rush])
data class HomeGoodVosEntity(
    val goodsVo: HomeGoodVoEntity,
    val marketingTimeDesc: String,
    val marketingTime: String,
    val marketingProgress: Double,
    val marketingVo: MarketVoEntity
) : HomePageEntity<LayoutHomeRushBinding>() {
    override fun getSpanSize(): Int = 1
    override fun sorted() = 8
}

@Serializable
data class MarketVoEntity(
    val groupedNumbers: Int,
    val marketingId: Int,
    val marketingName: String,
    val marketingPrice: Double,
    val marketingType: Int,
    val marketingVipPrice: Double,
    val ruleId: Int,
    val ruleName: String,
    val storeId: Int
)

@Serializable
data class HomeGoodVoEntity(
    val auditStatus: String,
    val fictitiousSalesCount: Int,
    val freightTemplateId: Int?,
    val goodsBrandVo: Int?,
    val goodsId: Int,
    val goodsInfoAdded: String,
    val goodsInfoAddedTime: String,
    val goodsInfoBarcode: String,
    val goodsInfoCostPrice: Double,
    val goodsInfoId: Int,
    val goodsInfoImgId: String,
    val goodsInfoIsbn: String,
    val goodsInfoItemNo: String,
    val goodsInfoMarketPrice: Double,
    val goodsInfoName: String,
    val goodsInfoPreferPrice: Double,
    val goodsInfoScorePrice: Int?,
    val goodsInfoStock: Int,
    val goodsInfoSubtitle: String,
    val goodsInfoUnaddedTime: Int?,
    val goodsInfoWeight: Double,
    val goodsSpecDetailVos: List<String>,
    val isCustomerDiscount: String,
    val isThird: String,
    val ismailbay: String,
    val offlineflag: Int,
    val refuseReason: String,
    val showList: String,
    val showMobile: String,
    val thirdId: Int,
    val thirdName: String
)

//推荐界面
@Serializable
data class HomeRecommendData(
    val goodsRecommends: List<HomeGoodsRecommendEntity>,
    val haveNext: Boolean
)

//推荐title
@LayoutView(layout = [R.layout.layout_home_recommend_title])
data class HomeRecommendTitle(val name: String) :
    HomePageEntity<LayoutHomeRecommendTitleBinding>() {
    override fun getSpanSize() = 1
    override fun sorted() = 9
}


@Serializable
@LayoutView(layout = [R.layout.layout_home_recommend])
data class HomeGoodsRecommendEntity(
    val goodsId: Int,
    val goodsName: String,
    val goodsPicture: String,
    val preferPrice: Double,
    val scorePrice: String?
) : HomePageEntity<LayoutHomeRecommendBinding>() {
    override fun getSpanSize() = 2
    override fun sorted(): Int = 10
}

