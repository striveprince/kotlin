package com.lifecycle.demo.ui.home.message

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.life.anko.recycler.diff.RecyclerAnkoFragment
import com.lifecycle.binding.util.recyclerAnko
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel
import io.reactivex.Single
import org.jetbrains.anko.AnkoContext

@Route(path = message)
class HomeMessageFragment : RecyclerAnkoFragment<HomeMessageEntity>(){
    companion object{ const val message = FragmentComponent.Config.fragment+"message" }

    override fun parse(t: ListDiffViewModel<HomeMessageEntity>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this,t).apply { t.httpData = {_,_->
            Single.just(arrayListOf(
                HomeMessageEntity(),
                HomeMessageEntity(),
                HomeMessageEntity(),
                HomeMessageEntity(),
                HomeMessageEntity(),
                HomeMessageEntity()
            ))
        } }
    }

}