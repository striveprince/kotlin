package com.customers.zktc.inject.data

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.binding.model.App
import com.customers.zktc.R
import com.customers.zktc.base.rxjava.exception.RestfulException
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import io.reactivex.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Api(
    private val context: Context,
    private val netApi: NetApi,
    private val databaseApi: DatabaseApi,
    private val mapApi: MapApi,
    private val ossApi: OssApi,
    private val preferenceApi: PreferenceApi
) {
    fun checkUpdate(): Observable<*> {
        val builder = PgyUpdateManager.Builder()
        return Observable.just(0)
            .delay(2, TimeUnit.SECONDS)
            .flatMap {
                Observable.create(ObservableOnSubscribe<AppBean> {emitter->
                    builder.setUpdateManagerListener(object : UpdateManagerListener {
                        override fun onUpdateAvailable(p0: AppBean) {
                            AlertDialog.Builder(context)
                                .setNegativeButton(R.string.cancel) { _,_ -> emitter.onError(RestfulException()) }
                                .setPositiveButton(R.string.update){_,_-> emitter.onNext(p0)}
                                .show()
                        }
                        override fun checkUpdateFailed(p0: Exception) {
                            emitter.onError(p0)
                        }
                        override fun onNoUpdateAvailable() {
                            emitter.onError(RestfulException(0, context.getString(R.string.noUpdate)))
                        }
                    })
                }) }
            .flatMap{
                netApi.download(it.downloadURL)
//                    .compose(ErrorTransform())
                    .toObservable()

            }
    }

}
