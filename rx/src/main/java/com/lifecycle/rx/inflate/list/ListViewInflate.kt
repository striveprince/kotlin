package com.lifecycle.rx.inflate.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.ListInflate
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.stateStart
import com.lifecycle.rx.adapter.RecyclerAdapter
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
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
    lateinit var propertyChangedCallback :Observable.OnPropertyChangedCallback
    override fun initBinding(t: Binding) { binding = t }

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply {
            propertyChangedCallback = loadingState.observe { getData(it) }
            loadingState.set(stateStart(AdapterType.refresh))
        }
    }

    private fun getData(state: Int) {
        if (canRun.getAndSet(false)&&isStateStart(state))
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
        canRun.compareAndSet(false, true)
    }

    override fun onSubscribe(job: Disposable) {
        this.job = job
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }

    fun destroy(){
        loadingState.removeOnPropertyChangedCallback(propertyChangedCallback)
    }
}