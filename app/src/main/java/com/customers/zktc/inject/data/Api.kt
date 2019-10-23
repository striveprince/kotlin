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
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import okio.Okio
import timber.log.Timber
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
    fun homePage(offset: Int, refresh: Int, pageCount: Int): Single<List<HomePageEntity<*>>> {
        val banner: Observable<out HomePageEntity<*>> = getOperationAd("ad_home_index_1")
        val category: Observable<HomePageEntity<*>> = netApi.operationHomeCategorys()
            .restful().map { it.operationHomeCategorys }.toObservable()
            .concatMap { Observable.fromIterable(it) }
        val homeSpecial: Observable<HomePageEntity<*>> = getOperationAd("ad_home_index_2")
            .zipWith(getOperationAd("ad_home_index_3")) { t1, t2 -> getPageArea(t1, t2) }
        val operationFloor = netApi.getOperationFloor(HomeFloorParams(""))
            .restful()
            .toObservable()
            .concatMap { Observable.fromIterable(converterFloorData(it)) }
        val homeGoodRecommend =
            netApi.homeGoodRecommend(HomeRecommendParams("goods_home_index_1", 1, 1, pageCount))
                .restful()
                .toObservable()
                .concatMap { Observable.fromIterable(converterGoodsRecommends(it)) }

        val rushList = netApi.getRushList(HomeRushListParams())
            .restful()
            .toObservable()
            .concatMap { Observable.fromIterable(converterRushList(it)) }
        return Observable.mergeArray(
            banner.noError(),
            category.noError(),
            homeSpecial.noError(),
            operationFloor.noError(),
            homeGoodRecommend.noError(),
            rushList.noError()
        ).toSortedList { t1, t2 -> t1.getSorted() - t2.getSorted()}
            .doOnSuccess{
                Timber.i("list=$it")
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getOperationAd(adPositionNumber: String): Observable<HomePageOperationData> {
        return netApi.getOperationAd(HomeOperationParams(adPositionNumber)).restful().toObservable()
    }

    private fun getPageArea(t1: HomePageOperationData, t2: HomePageOperationData): HomePageEntity<*> {
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

    private fun converterFloorData(it: HomeFloorData): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        for (floorType in it.operationFloorTypes) {
            list.add(HomeFloorDataEntity(floorType.name, floorType.pictureNumber, ArrayList()))
            list.addAll(floorType.operationFloors)
        }
        return list
    }

    private fun converterGoodsRecommends(it: HomeRecommendData): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        list.add(HomeRecommendTitle("为你推荐"))
        list.addAll(it.goodsRecommends)
        return list
    }

    private fun converterRushList(it: HomeGoodsVoData): ArrayList<HomePageEntity<*>> {
        val list = ArrayList<HomePageEntity<*>>()
        list.add(HomeRushTitle(""))
        list.addAll(it.goodsVos)
        return list
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
