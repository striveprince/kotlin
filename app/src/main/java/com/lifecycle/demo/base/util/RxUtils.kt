package com.lifecycle.demo.base.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.lifecycle.binding.permission.PermissionsUtil
import com.lifecycle.binding.permission.checkPermissions
import com.lifecycle.binding.util.startActivity
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.ApiException
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.NoPermissionException
import com.lifecycle.demo.inject.transform.flowable.DoErrorFlowTransformer
import com.lifecycle.demo.inject.transform.flowable.ErrorFlowTransformer
import com.lifecycle.demo.inject.transform.flowable.RestfulFlowTransformer
import com.lifecycle.demo.inject.transform.observable.DoErrorObservableTransformer
import com.lifecycle.demo.inject.transform.observable.ErrorObservableTransformer
import com.lifecycle.demo.inject.transform.observable.RestfulObserveTransformer
import com.lifecycle.demo.inject.transform.single.DoErrorTransformer
import com.lifecycle.demo.inject.transform.single.ErrorSingleTransformer
import com.lifecycle.demo.inject.transform.single.RestfulSingleTransformer
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


//----------------Single----------------------
fun <T> Single<T>.noError(): Observable<T> {
    return this.toObservable().compose(DoErrorObservableTransformer())
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

//----------------Observable----------------------

fun <T> Observable<T>.noError(): Observable<T> {
    return this.compose(DoErrorObservableTransformer())
}

fun <T> Observable<InfoEntity<T>>.restful(): Observable<T> {
    return this
        .compose(ErrorObservableTransformer())
        .compose(DoErrorObservableTransformer())
        .compose(RestfulObserveTransformer())
}

fun <T> Observable<InfoEntity<T>>.restfulUI(): Observable<T> {
    return this.restful()
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<InfoEntity<T>>.transformerUI(v: View? = null): Observable<InfoEntity<T>> {
    v?.isEnabled = false
    return this.compose(ErrorObservableTransformer())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { v?.isEnabled = true }
}


fun <T> Observable<T>.ioToMainThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.newToMainThread(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}


//-----------------Flowable---------------------

fun <T> Flowable<T>.noError(): Flowable<T> {
    return this.compose(DoErrorFlowTransformer())
}

fun <T> Flowable<InfoEntity<T>>.restful(): Flowable<T> {
    return this
        .compose(ErrorFlowTransformer())
        .compose(DoErrorFlowTransformer())
        .compose(RestfulFlowTransformer())
}

fun <T> Flowable<InfoEntity<T>>.restfulUI(): Flowable<T> {
    return this.restful()
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<InfoEntity<T>>.transformerUI(v: View? = null): Flowable<InfoEntity<T>> {
    v?.isEnabled = false
    return this.compose(ErrorFlowTransformer())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { v?.isEnabled = true }
}


fun <T> Flowable<T>.ioToMainThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.newToMainThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

//----------------Permission-------------------

fun <T> Observable<T>.checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<T> {
    return if (activity.checkPermissions(*permissions)) this
    else activity.rxPermissions(*permissions)
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
    else activity.rxPermissions(*permissions)
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
            .show()
    }.map { checkPermissions(*permissions) }
}