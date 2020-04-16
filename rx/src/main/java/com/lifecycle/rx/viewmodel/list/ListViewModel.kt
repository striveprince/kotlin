package com.lifecycle.rx.viewmodel.list

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.viewmodel.ListModel
import com.lifecycle.rx.adapter.RecyclerAdapter
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import com.lifecycle.rx.viewmodel.LifeViewModel
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewModel<E : Inflate>(final override val adapter: IList<E> = RecyclerAdapter()) :
    LifeViewModel(), IList<E>, Observer<List<E>>, ListModel<E, Disposable> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override val error = MutableLiveData<Throwable>()
    override val adapterList: MutableList<E> = adapter.adapterList
    override var job: Disposable? = null
    var httpData: (Int, Int) -> Single<List<E>> = { _, _ -> Single.just(ArrayList()) }
    override val canRun: AtomicBoolean = AtomicBoolean(true)
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        super.attachData(owner, bundle)
        loadingState.observer(owner) { getData(it) }
        loadingState.value = AdapterType.refresh
    }


    open fun getData(it: Int) {
        if (isStateStart(it) && canRun.getAndSet(false)) {
            Timber.i("SmartRefreshState=${isStateStart(it)} ,result=$it")
            httpData(getStartOffset(it), it)
                .ioToMainThread()
                .map { if (it is ArrayList) it else ArrayList(it) }
                .subscribe(NormalObserver(this))
        }
    }

    override fun onNext(t: List<E>) {
        super.onNext(t)
    }

    override fun onComplete() {
        super.onComplete()
        job?.dispose()
        canRun.compareAndSet(false, true)
    }

    override fun onSubscribe(job: Disposable) {
        this.job = job
        disposables.add(job)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }

}