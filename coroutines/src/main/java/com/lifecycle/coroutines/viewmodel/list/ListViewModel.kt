package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.ListModel
import com.lifecycle.coroutines.IListAdapter
import com.lifecycle.coroutines.adapter.RecyclerAdapter
import com.lifecycle.coroutines.util.launchIo
import com.lifecycle.coroutines.util.launchMain
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Job

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>,ListModel<E, Any, Job> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override val error = MutableLiveData<Throwable>()
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    var httpData: (Int, Int) -> MutableList<E> = { _, _ -> ArrayList() }

    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) { doGetData(it) }
        loadingState.value = stateStart(AdapterType.refresh)
    }

    open fun doGetData(it: Int) {
        if (it != 0 && !isStateRunning(it)) {
            onSubscribe(launchIo {
                try {
                    val list = httpData(getStartOffset(it), it)
                    launchMain { onNext(list) }
                } catch (e: Exception) {
                    launchMain { onError(e) }
                } finally {
                    launchMain { onComplete() }
                }
            })
        }
    }

    override fun onSubscribe(job: Job) {
        this.job = job
        jobs.add(job)
    }

    override fun onNext(t: MutableList<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, it)
            loadingState.value = stateSuccess(it)
        }
    }

    override fun onError(e: Throwable) {
        error.value = e
        loadingState.value = stateError(loadingState.value!!)
    }


}
