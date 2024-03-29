package com.lifecycle.rx.viewmodel.list

import android.os.Bundle
import android.util.SparseArray
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterType.no
import com.lifecycle.binding.adapter.AdapterType.refresh
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.list.ListModel
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.stateStart
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import com.lifecycle.rx.viewmodel.LifeViewModel
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, Observer<List<E>>, ListModel<E, Disposable> {
    override val array: SparseArray<Any> = SparseArray()
    override val events: ArrayList<IEvent<E>> = adapter.events
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(stateStart(refresh))
    override val adapterList: MutableList<E> = adapter.adapterList
    override var job: Disposable? = null
    override val errorMessage: MutableLiveData<CharSequence> = MutableLiveData("")
    var httpData: (Int, Int) -> Single<List<E>> = { _, _ -> Single.just(ArrayList()) }
    override val state: AtomicInteger = AtomicInteger(no)
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        super.attachData(owner, bundle)
        loadingState.observer(owner) {
            Timber.i("loadingState = $it state = ${state.get()}")
            if (state.getAndSet(it) != it && isStateStart(it)) getData(it)
        }
    }

    override fun getData(state: Int) {
        super.getData(state)
        httpData(getStartOffset(state), state)
            .ioToMainThread()
            .map { if (it is ArrayList) it else ArrayList(it) }
            .subscribe(NormalObserver(this))
    }

    override fun onNext(t: List<E>) {
        super.onNext(t)
    }

    override fun onComplete() {
        super.onComplete()
        job?.dispose()
    }

    override fun onSubscribe(job: Disposable) {
        this.job = job
        disposables.add(job)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }

}