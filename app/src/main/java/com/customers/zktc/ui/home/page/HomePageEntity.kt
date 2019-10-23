package com.customers.zktc.ui.home.page

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.recycler.RecyclerAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.ViewGridInflate
import com.customers.zktc.R
import com.customers.zktc.databinding.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:15
 * Email: 1033144294@qq.com
 */

open class HomePageEntity<Binding : ViewDataBinding> : ViewGridInflate<Binding>() {
    override fun getSpanSize() = 1
    open fun getSorted():Int = 0
}

//banner图界面
@Serializable
@LayoutView(layout = [R.layout.layout_home_banner])
class HomePageOperationData(val operationAds: List<HomePageOperationEntity>) :
    HomePageEntity<LayoutHomeBannerBinding>() {
    @Transient
    val adapter = RecyclerAdapter<HomePageOperationEntity>()
    override fun getSpanSize() = 1
    override fun bindView(context: Context, binding: LayoutHomeBannerBinding) {
        super.bindView(context, binding)
        adapter.addListAdapter(0, operationAds)
    }
}

@LayoutView(layout = [R.layout.layout_home_banner_item])
@Serializable
data class HomePageOperationEntity(
    val id: Int = 0,
    val name: String = "",
    val picture: String = "",
    val viceName: String = "",
    val orderBy: Int = 0,
    val linkUrl: String = ""
) : HomePageEntity<LayoutHomeBannerItemBinding>() {
    override fun getSpanSize() = 1
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
    override fun getSorted()=2
}

//活动专区界面
@LayoutView(layout = [R.layout.layout_home_area])
class HomePageAreaData(val operationAds: List<HomePageOperationEntity>) :
    HomePageEntity<LayoutHomeAreaBinding>() {
    override fun getSpanSize() = 1
    override fun getSorted()=3
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
    override fun getSorted()=4
}

@LayoutView(layout = [R.layout.layout_home_floor])
@Serializable
data class HomeFloorTypeEntity(
    val floorLinkUrl: String,
    val floorName: String,
    val floorPicture: String,
    val floorViceName: String,
    val id: Int,
    val orderBy: Int,
    var pictureNumber: Int=1
) : HomePageEntity<LayoutHomeFloorBinding>() {
    override fun getSpanSize() = pictureNumber
    override fun getSorted()=4
}

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
    override fun getSorted()=6
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
    override fun getSorted(): Int =7
}

@Serializable
data class HomeGoodsVoData(
    val goodsVos: List<HomeGoodVosEntity>
)


@LayoutView(layout = [R.layout.layout_home_rush_title])
data class HomeRushTitle(val name: String) : HomePageEntity<LayoutHomeRushTitleBinding>() {
    override fun getSpanSize() = 1
    override fun getSorted()=8
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
    override fun getSorted()=9
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





