package com.customers.zktc.inject.data.net

import android.app.Activity
import android.app.AlertDialog
import com.binding.model.createWholeDir
import com.binding.model.ioToMainThread
import com.binding.model.toEntities
import com.customers.zktc.R
import com.customers.zktc.base.util.noError
import com.customers.zktc.base.util.restful
import com.customers.zktc.base.util.restfulCompose
import com.customers.zktc.inject.data.net.bean.*
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.cart.HomeCartStoreEntity
import com.customers.zktc.ui.home.cart.HomeShoppingParams
import com.customers.zktc.ui.home.page.*
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignParams
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import okio.Okio
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/29 14:54
 * Email: 1033144294@qq.com
 */
class NetApi(private val httpApi: HttpApi) {
    fun homePage(offset: Int, pageCount: Int, homePageBanner: HomePageBanner):
            Single<List<HomePageInflate<*>>> {
        val banner: Observable<out HomePageInflate<*>> = getOperationAd("ad_home_index_1")
            .map {
                homePageBanner.operationAds = it
                homePageBanner
            }
        val category: Observable<HomePageInflate<*>> = httpApi.operationHomeCategorys()
            .restful()
            .map { it.operationHomeCategorys }
            .toObservable()
            .concatMap { Observable.fromIterable(it) }
            .map { HomeCategoryEntity(it) }
        val homeSpecial: Observable<HomePageInflate<*>> = getOperationAd("ad_home_index_2")
            .zipWith(getOperationAd("ad_home_index_3")) { t1, t2 -> getPageArea(t1, t2) }
        val operationFloor = httpApi.getOperationFloor(HomeFloorParams(""))
            .restful()
            .toObservable()
            .map { converterFloorData(it.operationFloorTypes) }
            .concatMapIterable { it }
//        val mallRecommend: Observable<HomePageInflate<*>> = getOperationAd("ad_home_index_4")
//            .map { converterMall(it) }
        val mallRecommend: Observable<HomePageInflate<*>> = getRecommendGoodsList(1)
        val marketingList: Observable<HomePageInflate<*>> =
            httpApi.marketingList(HomeRushListParams())
                .restful()
                .toObservable()
                .concatMap { Observable.fromIterable(converterMarketing(it.goodsVos)) }
        val rushList = httpApi.getRushList(HomeRushParams(pageNo = offset))
            .restful()
            .toObservable()
            .concatMapIterable { it.goodsVos }
            .map { HomeGoodVosEntity(it) }
            .toList().toObservable()
            .map { convertHomeAssemble(it) }
        val homeGoodRecommend = getRecommend(offset, pageCount)
            .toObservable()
            .concatMap { Observable.fromIterable(converterGoodsRecommends(it)) }
        return Observable.mergeArray(
            banner.noError(),
            category.noError(),
            operationFloor.noError(),
            homeSpecial.noError(),
            mallRecommend.noError(),
            marketingList.noError(),
            rushList.noError(),
            homeGoodRecommend.noError()
        ).toSortedList { t1, t2 -> t1.sorted() - t2.sorted() }
            .flatMap<List<HomePageInflate<*>>> {
                if (it.isEmpty()) Single.just(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap {
                        val single: SingleSource<List<HomePageInflate<*>>> = Single.error(
                            ApiException("请求失败")
                        )
                        single
                    }
                else Single.just(it)
            }
    }

    private fun convertHomeAssemble(it: List<HomeGoodVosEntity>): HomePageInflate<*> {
        if(it.isEmpty())throw ApiException()
        return HomeAssembleEntity(it)
    }

    private fun getRecommendGoodsList(i: Int): Observable<HomePageInflate<*>> =
        httpApi.getRecommendGoodsList(HomeGoodsRecommend(i)).restful()
            .map { convertStoreRecommend(it) }
            .toObservable()

    private fun convertStoreRecommend(it: HomePageRecommendData): HomePageInflate<*> {
        if (it.recommendGoodsList.isEmpty()) throw ApiException()
        val list = ArrayList<HomeGoodsRecommendListEntity>()
        for (homeGoodsRecommendListBean in it.recommendGoodsList)
            list.add(HomeGoodsRecommendListEntity(homeGoodsRecommendListBean))
        return StoreInfoEntity(it.storeInfo, list)
    }


    private fun converterMarketing(goodsVos: List<HomeGoodVosBean>): ArrayList<HomePageInflate<*>> {
        val list = ArrayList<HomePageInflate<*>>()
        val title = HomePageTitle()
        list.add(title)
        for (goodsVo in goodsVos) {
            list.add(HomeGoodVosEntity(goodsVo))
        }
        return list
    }

    fun getRecommend(offset: Int, pageCount: Int): Single<List<HomePageInflate<*>>> =
        httpApi.homeGoodRecommend(
            HomeRecommendParams("goods_home_index_1", offset, offset, pageCount)
        )
            .restful().toObservable()
            .concatMap { Observable.fromIterable(it.goodsRecommends) }
            .map { HomeGoodsRecommendEntity(it) as HomePageInflate<*> }
            .toList()

    private fun getOperationAd(adPositionNumber: String): Observable<List<HomePageOperationEntity>> {
        return httpApi.getOperationAd(HomeOperationParams(adPositionNumber))
            .restful()
            .toObservable()
            .concatMap { Observable.fromIterable(it.operationAds) }
            .map { HomePageOperationEntity(it) }
            .toList().toObservable()
    }

    private fun converterFloorData(it: List<HomeFloorDataBean>): ArrayList<HomePageInflate<*>> {
        val list = ArrayList<HomePageInflate<*>>()
        for (operationFloorType in it) {
            list.add(HomeFloorDataEntity(operationFloorType))
            for (floorType in operationFloorType.operationFloors) {
                val entity = HomeFloorTypeEntity(floorType)
                entity.layoutIndex = operationFloorType.pictureNumber - 1
                list.add(entity)
            }
        }
        return list
    }

    private fun getPageArea(
        t1: List<HomePageOperationEntity>,
        t2: List<HomePageOperationEntity>
    ): HomePageInflate<*> {
        val list = ArrayList<HomePageOperationEntity>()
        for (index in 0..3) {
            when (index) {
                0 -> if (t1.isNotEmpty()) list.add(t1[0])
                else list.add(HomePageOperationEntity(HomePageOperationBean()))
                1 -> if (t2.isNotEmpty()) list.add(t2[0])
                else list.add(HomePageOperationEntity(HomePageOperationBean()))
                else -> if (t2.size > 1) list.add(t2[1])
                else list.add(HomePageOperationEntity(HomePageOperationBean()))
            }
        }
        return HomePageAreaData(list)
    }

    private fun converterGoodsRecommends(it: List<HomePageInflate<*>>): ArrayList<HomePageInflate<*>> {
        val list = ArrayList<HomePageInflate<*>>()
        list.add(HomePageTitle(1))
        list.addAll(it)
        return list
    }

    private fun converterMall(it: List<HomePageOperationEntity>): HomeMallEntity {
        if (it.isEmpty()) throw ApiException("")
        for (operationAd in it) {
            operationAd.layoutIndex = 1
        }
        return HomeMallEntity(it)
    }


    fun checkUpdate(context: Activity): Single<File> {
        val builder = PgyUpdateManager.Builder()
        return Single.just(0)
            .delay(2, TimeUnit.SECONDS)
            .flatMap {
                Single.create(SingleOnSubscribe<AppBean> { emitter ->
                    builder.setUpdateManagerListener(object : UpdateManagerListener {
                        override fun onUpdateAvailable(p0: AppBean) {
                            updateDialog(context, emitter, p0)
                        }

                        override fun checkUpdateFailed(p0: Exception) {
                            emitter.onError(p0)
                        }

                        override fun onNoUpdateAvailable() {
                            emitter.onError(ApiException("", context.getString(R.string.noUpdate)))
                        }
                    })
                })
            }
            .ioToMainThread()
            .flatMap { download(it) }
    }

    private fun updateDialog(context: Activity, emitter: SingleEmitter<AppBean>, p0: AppBean) {
        AlertDialog.Builder(context)
            .setNegativeButton(R.string.cancel) { d, _ ->
                d.dismiss()
                emitter.onError(ApiException())
            }
            .setPositiveButton(R.string.update) { d, _ ->
                emitter.onSuccess(p0)
                d.dismiss()
            }
            .show()
    }

    private fun download(it: AppBean): Single<File> {
        val fileName = "/update/zktc_" + it.versionName + ".apk"
        return httpApi.download(0, fileName, it.downloadURL)
            .map {
                val file = File(createWholeDir(fileName))
                val sink = Okio.buffer(Okio.sink(file))
                sink.writeAll(it.source())
                sink.close()
                file
            }.ioToMainThread()
    }


    fun wechatLogin(params: SignParams): Single<UserEntity> {
        return httpApi.wechatLogin(params)
            .restfulCompose()
    }

    fun registerCode(mobile: SignParams): Single<CodeEntity> {
        return httpApi.registerCode(mobile).restfulCompose()
    }

    fun loginCode(params: SignParams): Single<CodeEntity> {
        return httpApi.loginCode(params)
            .restfulCompose()
    }

    fun modifyPassword(): Single<String> {
        return httpApi.modifyPassword()
    }

    fun passwordLogin(params: SignParams) = httpApi.passwordLogin(params)
        .restful()

    fun codeLogin(params: SignParams) = httpApi.codeLogin(params)
        .restful()


    fun register(signParams: SignParams) = httpApi.register(signParams)
        .restful()

    fun shoppingCartList() = httpApi.shoppingCartList(HomeShoppingParams())
        .restful()
        .map { it.storeList.toEntities<HomeCartStoreEntity>() }

    fun getUnReadMessage()= httpApi.getUnReadMessage(HomeRushListParams())
            .restful()

    fun rank()=httpApi.rank(HomeRushListParams()).restful()

    fun getCustomerIndexInfo()=httpApi.getCustomerIndexInfo(HomeRushListParams()).restful()
}