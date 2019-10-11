package com.customers.zktc.inject.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.GridInflate
import com.binding.model.createWholeDir
import com.customers.zktc.R
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import okio.Okio
import java.io.File
import java.util.concurrent.TimeUnit

class Api(
    private val application: Context,
    private val netApi: NetApi,
    private val databaseApi: DatabaseApi,
    private val mapApi: MapApi,
    private val ossApi: OssApi,
    private val preferenceApi: PreferenceApi) {

    fun homePage(offset: Int, refresh: Int): Single<List<GridInflate<in ViewDataBinding>>> {
        netApi.banner()
        return Single.just(ArrayList())
    }

    fun checkUpdate(context: Activity): Single<File> {
        val builder = PgyUpdateManager.Builder()
        return Single.just(0)
            .delay(2, TimeUnit.SECONDS)
            .flatMap {
                Single.create(SingleOnSubscribe  <AppBean> { emitter ->
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
        return netApi.download(0,fileName,it.downloadURL)
            .map {
                val file = File(createWholeDir(fileName))
                val sink = Okio.buffer(Okio.sink(file))
                sink.writeAll(it.source())
                sink.close()
                file
            }
    }

    fun locationCity(activity: AppCompatActivity): Observable<String> {
        return mapApi.locationCity(activity)
    }


}
