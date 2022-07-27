package com.lifecycle.rx.inflate

import android.content.Context
import android.util.SparseArray
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.AdapterType.no
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.list.ListInflate
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

open class ListViewInflate<E : Inflate, Binding : ViewDataBinding>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    IListAdapter<E>, ListInflate<E, Binding, Disposable>, Observer<List<E>> {
    override val array: SparseArray<Any> = SparseArray()
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val adapterList: MutableList<E> = adapter.adapterList
    override val events: ArrayList<IEvent<E>> = adapter.events
    override val loadingState: ObservableInt = ObservableInt(no)
    lateinit var binding: Binding
    override val errorMessage: ObservableField<CharSequence> = ObservableField()
    var httpData: (Int, Int) -> Single<List<E>> = { _, _ -> Single.just(ArrayList<E>()) }
    override var job: Disposable? = null
    override var callback: Observable.OnPropertyChangedCallback? = null
    override val state: AtomicInteger = AtomicInteger(no)

    override fun initBinding(context: Context, t: Binding) {
        binding = t
    }

    override fun getData(state: Int) {
        super.getData(state)
        httpData(getStartOffset(state), state)
            .ioToMainThread()
            .map { if (it is ArrayList) it else ArrayList(it) }
            .subscribe(NormalObserver(this))
    }

    override fun onComplete() {
        super.onComplete()
        job?.dispose()
    }

    override fun onSubscribe(job: Disposable) {
        super.onSubscribe(job)
    }

    override fun onNext(t: List<E>) {
        super.onNext(t)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }
}