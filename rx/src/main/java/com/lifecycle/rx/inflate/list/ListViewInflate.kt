package com.lifecycle.rx.inflate.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.ListInflate
import com.lifecycle.binding.util.canStateStart
import com.lifecycle.binding.util.isStateRunning
import com.lifecycle.rx.adapter.RecyclerAdapter
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewInflate<E : Inflate, Binding : ViewDataBinding>(final override val adapter: IList<E> = RecyclerAdapter()) :
    BindingInflate<Binding>, IList<E>, Observer<List<E>>, ListInflate<E, Disposable> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val adapterList: MutableList<E> = adapter.adapterList
    override var job: Disposable? = null
    var httpData: (Int, Int) -> Single<List<E>> = { _, _ -> Single.just(ArrayList()) }
    override val loadingState: ObservableInt = ObservableInt(AdapterType.no)
    override val error: ObservableField<Throwable> = ObservableField()
    lateinit var binding: Binding
    override val canRun: AtomicBoolean = AtomicBoolean(true)

    override fun initBinding(t: Binding) {
        binding = t
    }

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply {
            loadingState.set(AdapterType.refresh)
            doGetData(AdapterType.refresh)
            loadingState.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: androidx.databinding.Observable, propertyId: Int) {
                    if(sender is ObservableInt)getData(sender.get())
                }
            })
        }
    }

    open fun doGetData(it: Int) {
        if (canStateStart(it)) getData(it)
    }

    private fun getData(it: Int){
        Timber.i("do get data state = $it")
        if(canRun.getAndSet(false))
        httpData(getStartOffset(it), it)
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
        canRun.compareAndSet(false,true)
    }

    override fun onSubscribe(job: Disposable) {
        this.job = job
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }

}