package com.lifecycle.rx.inflate.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.ListInflate
import com.lifecycle.rx.IListAdapter
import com.lifecycle.rx.adapter.RecyclerAdapter
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewInflate<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    Inflate, IListAdapter<E>, Observer<List<E>>,ListInflate<E,Observable<Any>,Disposable> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
//    override val loadingState= MutableLiveData(AdapterType.no)
//    override val error = MutableLiveData<Throwable>()
    override val adapterList: MutableList<E> = adapter.adapterList
    override var job: Disposable?=null
    var httpData :(Int,Int)->Single<List<E>> = {_,_->Single.just(ArrayList())}
    override var canRun: AtomicBoolean = AtomicBoolean(true)
    override val loadingState: ObservableInt = ObservableInt(AdapterType.no)
    override val error: ObservableField<Throwable> = ObservableField()

//    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
//        super.attachData(owner, bundle)
//        loadingState.observer(owner) { doGetData(it) }
//        loadingState.value = AdapterType.refresh
//    }

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply {
            loadingState.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: androidx.databinding.Observable, propertyId: Int) {
//                    callback.onPropertyChanged(sender, arg);
                }
            })
        }
    }

    open fun doGetData(it:Int){
        if (it!=0 && canRun.getAndSet(false)) {
            httpData(getStartOffset(it), it)
                .ioToMainThread()
                .map { if(it is ArrayList)it else ArrayList(it) }
                .subscribe(NormalObserver(this))
        }
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
    }

    override fun onError(e: Throwable) {
       super.onError(e)
    }

}