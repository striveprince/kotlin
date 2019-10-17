package com.binding.model.inflate.model

import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListUpdateCallback
import com.binding.model.base.container.Container
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.obj.RecyclerEvent
import com.binding.model.inflate.obj.RecyclerStatus
import com.binding.model.inflate.observer.NormalObserver
import com.binding.model.pageWay
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

open class ViewHttpModel<T : CycleContainer<*>, Binding : ViewDataBinding, R> : ViewModel<T, Binding>(), Observer<R> {
    private val normalObserver by lazy { NormalObserver(this) }
    val loading = ObservableBoolean(false)
    val enable = ObservableBoolean(true)
    val error = ObservableField("")
    internal var offset = 0
    var pageCount = 16
    var headIndex = 0
    var http: ((Int, Int) -> Single<R>)? = null
    var refresh = 0



    fun setRxHttp(http: ((Int, Int) -> Single<R>) ){
        this.http = http
        onHttp(0,RecyclerStatus.init)
    }

    fun onHttp(offset: Int, @RecyclerEvent refresh: Int) {
        this.refresh = refresh
        this.offset = if (refresh > 0) 0 else offset
        val o = if (offset > headIndex) offset - headIndex else 0
        val p = if (pageWay) o / pageCount + 1 else o
        http?.invoke(offset, refresh)?.subscribe(normalObserver)
    }

    override fun onNext(t: R) {

    }

    override fun onComplete() {
        loading.set(false)
    }

    override fun onSubscribe(d: Disposable) {
        loading.set(true)
        addDisposables(d)
    }


    override fun onError(e: Throwable) {
        loading.set(false)
        error.set(if (TextUtils.isEmpty(e.message)) "获取数据失败" else e.message)
    }

    fun isRefresh(): Boolean {
        return refresh > 0
    }

}