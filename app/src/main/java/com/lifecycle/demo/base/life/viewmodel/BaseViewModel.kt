package com.lifecycle.demo.base.life.viewmodel

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.exception.ApiEmptyException
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.viewmodel.LifeViewModel
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel
import com.lifecycle.binding.viewmodel.list.ListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import io.reactivex.Single
import timber.log.Timber

open class BaseViewModel : LifeViewModel<LifecycleOwner, Api>() {
    val title = MutableLiveData<CharSequence>("")
    lateinit var api: Api
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        this.api = api
        super.initData(api, owner, bundle)
    }
}

open class BaseLifeViewModel<T : LifecycleOwner> : LifeViewModel<T, Api>() {
    val title = MutableLiveData<CharSequence>("")
    lateinit var api: Api
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        this.api = api
        super.initData(api, owner, bundle)
    }
}

abstract class BaseDiffListViewModel<E : DiffInflate> : ListDiffViewModel<LifecycleOwner, E, Api>() {
    lateinit var api: Api
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        this.api = api
        super.initData(api, owner, bundle)
    }
}

abstract class BaseListViewModel<E : Inflate> : ListViewModel<LifecycleOwner, E, Api>() {
    lateinit var api: Api
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        this.api = api
        super.initData(api, owner, bundle)
    }
}

open class RecyclerModel<E : DiffInflate> : BaseDiffListViewModel<E>() {
    override fun onComplete() {
        super.onComplete()
        error.value = if (adapterList.isNotEmpty())null else ApiEmptyException()
    }
//
//    fun onTestClick(v:View){
//        loadingState.value = when(loadingState.value){
//            AdapterType.no->AdapterType.refresh
//            AdapterType.refresh->AdapterType.load
//            else -> AdapterType.no
//        }
//        Timber.i("state=${loadingState.value}")
//    }


    fun onLoadMore(refreshLayout:RefreshLayout){
        doGetData(AdapterType.refresh,api)
    }

    fun onRefresh(refreshLayout:RefreshLayout){
        doGetData(AdapterType.load,api)
    }
}