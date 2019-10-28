package com.customers.zktc.inject.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.binding.model.createWholeDir
import com.binding.model.ioToMainThread
import com.customers.zktc.R
import com.customers.zktc.base.util.noError
import com.customers.zktc.base.util.restful
import com.customers.zktc.base.util.restfulCompose
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi
import com.customers.zktc.inject.data.preference.user.UserEntity
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

class Api(
    private val application: Context,
    private val netApi: NetApi,
    private val databaseApi: DatabaseApi,
    private val mapApi: MapApi,
    private val ossApi: OssApi,
    private val preferenceApi: PreferenceApi
) {
    fun homePage(offset: Int, refresh: Int, pageCount: Int, homePageBanner: HomePageBanner
    ): Single<List<HomePageInflate<*>>> {
        val banner: Observable<out HomePageInflate<*>> = getOperationAd("ad_home_index_1")
            .map {
                homePageBanner.operationAds = (it.operationAds)
                homePageBanner
            }
        val category: Observable<HomePageEntity<*>> = netApi.operationHomeCategorys()
            .restful().map { it.operationHomeCategorys }.toObservable()
            .concatMap { Observable.fromIterable(it) }
        val homeSpecial: Observable<HomePageEntity<*>> = getOperationAd("ad_home_index_2")
            .zipWith(getOperationAd("ad_home_index_3")) { t1, t2 -> getPageArea(t1, t2) }
        val operationFloor = netApi.getOperationFloor(HomeFloorParams(""))
            .restful()
            .toObservable()
            .concatMap { Observable.fromIterable(converterFloorData(it)) }
        val mallRecommend: Observable<HomePageInflate<*>> = getOperationAd("ad_home_index_4")
            .map { converterMall(it) }
        val marketingList: Observable<HomePageInflate<*>> =
            netApi.marketingList(HomeRushListParams())
                .restful()
                .toObservable()
                .concatMap { Observable.fromIterable(converterMarketing(it.goodsVos)) }
        val rushList =  netApi.getRushList(HomeRushParams(pageNo = offset))
            .restful()
            .map { HomeAssembleEntity(it.goodsVos) }
            .toObservable()
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
                        val single:SingleSource<List<HomePageInflate<*>>> = Single.error(ApiException("请求失败"))
                        single
                    }
                else Single.just(it)
            }
    }

    private fun converterMarketing(goodsVos: List<HomeGoodVosEntity>): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        val title = HomePageTitle()
        list.add(title)
        list.addAll(goodsVos)
        return list
    }

    fun getRecommend(offset: Int, pageCount: Int) =
        netApi.homeGoodRecommend(HomeRecommendParams("goods_home_index_1", offset, offset, pageCount))
            .restful()

    private fun getOperationAd(adPositionNumber: String): Observable<HomePageOperationData> {
        return netApi.getOperationAd(HomeOperationParams(adPositionNumber))
            .restful()
            .toObservable()
    }

    private fun converterFloorData(it: HomeFloorData): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        for (floorType in it.operationFloorTypes) {
            list.add(HomeFloorDataEntity(floorType.name, floorType.pictureNumber, ArrayList()))
            for (operationFloor in floorType.operationFloors) {
                operationFloor.pictureNumber = floorType.pictureNumber
            }
            list.addAll(floorType.operationFloors)
        }
        return list
    }

    private fun getPageArea(
        t1: HomePageOperationData,
        t2: HomePageOperationData
    ): HomePageEntity<*> {
        val list = ArrayList<HomePageOperationEntity>()
        for (index in 0..3) {
            when (index) {
                0 -> if (t1.operationAds.isNotEmpty()) list.add(t1.operationAds[0])
                else list.add(HomePageOperationEntity())
                1 -> if (t2.operationAds.isNotEmpty()) list.add(t2.operationAds[0])
                else list.add(HomePageOperationEntity())
                else -> if (t2.operationAds.size > 1) list.add(t2.operationAds[1])
                else list.add(HomePageOperationEntity())
            }
        }
        return HomePageAreaData(list)
    }

    private fun converterGoodsRecommends(it: HomeRecommendData): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        list.add(HomePageTitle(1))
        list.addAll(it.goodsRecommends)
        return list
    }

    private fun converterMall(it: HomePageOperationData): HomeMallEntity {
        if (it.operationAds.isEmpty()) throw ApiException("")
        for (operationAd in it.operationAds) {
            operationAd.layoutIndex = 1
        }
        return HomeMallEntity(it.operationAds)
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
        return netApi.download(0, fileName, it.downloadURL)
            .map {
                val file = File(createWholeDir(fileName))
                val sink = Okio.buffer(Okio.sink(file))
                sink.writeAll(it.source())
                sink.close()
                file
            }.ioToMainThread()
    }

    fun locationCity(activity: AppCompatActivity): Observable<String> {
        return mapApi.locationCity(activity)
    }

    fun passwordLogin(params: SignParams): Single<UserEntity> {
        return netApi.passwordLogin(params)
            .restful()
            .map { preferenceApi.login(it) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun wechatLogin(params: SignParams): Single<UserEntity> {
        return netApi.wechatLogin(params)
            .restfulCompose()
    }

    fun registerCode(mobile: SignParams): Single<CodeEntity> {
        return netApi.registerCode(mobile).restfulCompose()
    }

    fun codeLogin(params: SignParams): Single<UserEntity> {
        return netApi.codeLogin(params)
            .restful()
            .map { preferenceApi.login(it) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loginCode(params: SignParams): Single<CodeEntity> {
        return netApi.loginCode(params)
            .restfulCompose()
    }

    fun modifyPassword(): Single<String> {
        return netApi.modifyPassword()
    }

    fun register(signParams: SignParams): Single<UserEntity> {
        return netApi.register(signParams)
            .restful()
            .map { preferenceApi.login(it) }
            .observeOn(AndroidSchedulers.mainThread())
    }
}
