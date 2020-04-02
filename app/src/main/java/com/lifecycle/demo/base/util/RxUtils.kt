package com.lifecycle.demo.base.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.lifecycle.binding.permission.PermissionsUtil
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.ApiException
import com.lifecycle.demo.inject.NoPermissionException
import com.lifecycle.demo.inject.transform.single.DoErrorTransformer
import com.lifecycle.demo.inject.transform.single.ErrorSingleTransformer
import com.lifecycle.demo.inject.transform.observable.NoErrorObservableTransformer
import com.lifecycle.demo.inject.transform.single.RestfulSingleTransformer
import com.lifecycle.demo.inject.transform.flowable.DoErrorFlowTransformer
import com.lifecycle.demo.inject.transform.flowable.ErrorFlowTransformer
import com.lifecycle.demo.inject.transform.flowable.RestfulFlowTransformer
import com.lifecycle.rx.util.ioToMainThread
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun <T> Single<T>.noError(): Observable<T> {
    return this.toObservable().compose(NoErrorObservableTransformer())
}

fun <T> Single<InfoEntity<T>>.restful(): Single<T> {
    return this
        .compose(ErrorSingleTransformer())
        .compose(DoErrorTransformer())
        .compose(RestfulSingleTransformer())
}

fun <T> Single<InfoEntity<T>>.restfulUI(): Single<T> {
    return this.restful()
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<InfoEntity<T>>.transformerUI(v: View? = null): Single<InfoEntity<T>> {
    v?.isEnabled = false
    return this.compose(ErrorSingleTransformer())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { v?.isEnabled = true }
}


fun <T> Single<T>.ioToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.newToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.noError(): Observable<T> {
    return this.compose(NoErrorObservableTransformer())
}
fun<T> Flowable<InfoEntity<T>>.restful(): Flowable<T> {
    return this.compose(ErrorFlowTransformer())
        .compose(DoErrorFlowTransformer())
        .compose(RestfulFlowTransformer())
}
fun<T> Flowable<InfoEntity<T>>.restfulUI(): Flowable<T> {
    return this.restful().ioToMainThread()
}

fun <T> Observable<T>.checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<T> {
    return if (activity.checkPermissions(*permissions)) this
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) this
            else Observable.error(
                ApiException(1, activity.getString(R.string.no_permission))
            )
        }
}

fun checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<Boolean> {
    return if (activity.checkPermissions(*permissions)) Observable.just(true)
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) Observable.just(true)
            else Observable.error(
                ApiException(1, activity.getString(R.string.no_permission))
            )
        }
}

fun Activity.rxPermissions(vararg permissions: String): Observable<Boolean> {
    return Observable.create<Boolean> { emitter -> PermissionsUtil(this as FragmentActivity).request(*permissions) { emitter.onNext(it) } }
        .doOnNext { if (!it) throw NoPermissionException() }
}

fun Activity.rxPermissionsDialog(vararg permissions: String): Observable<Boolean> {
    return rxPermissions(*permissions)
        .flatMap {
            if (it) Observable.just(it)
            else showPermissionDialog(*permissions)
        }
}

private fun Activity.showPermissionDialog(vararg permissions: String): Observable<Boolean> {
    startActivity<PhoneSystemManager>(PhoneSystemManager.type to PhoneSystemManager.permission)
    return Observable.create<Any> { emitter ->
        MaterialDialog(this)
            .title(R.string.request_permission)
            .positiveButton { PhoneSystemManager.emitter = emitter }
            .show {}
    }.map { checkPermissions(*permissions) }
}