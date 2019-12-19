package com.lifecycle.demo.base.life.recycler

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.demo.base.life.anko.AnkoFragment
import com.lifecycle.demo.base.life.viewmodel.RecyclerModel
import com.lifecycle.demo.base.util.recyclerAnko
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.exception.ApiEmptyException
import com.lifecycle.demo.inject.data.net.exception.ApiException
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.DiffInflate
import io.reactivex.Single
import org.jetbrains.anko.AnkoContext

@Suppress("UNCHECKED_CAST")
abstract class RecyclerAnkoFragment<E : DiffInflate> : AnkoFragment<RecyclerModel<E>>() {
    internal var loadMore = true
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        model.httpData = { _, position, state ->
            if(state == AdapterType.add&&!loadMore) Single.error(ApiEmptyException())
            else apiData(position, state)
        }
        super.initData(api, owner, bundle)
    }

    abstract fun apiData(offset: Int, type: Int): Single<List<E>>


    override fun parse(t: RecyclerModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this, t)
    }

    override fun initModel(): RecyclerModel<E> {
        return ViewModelProviders.of(this)[RecyclerModel::class.java] as RecyclerModel<E>
    }
}