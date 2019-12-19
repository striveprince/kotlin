package com.lifecycle.demo.ui.home.message

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.base.life.recycler.RecyclerAnkoFragment
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import com.lifecycle.binding.inter.inflate.DiffInflate
import io.reactivex.Single

@Route(path = message)
class HomeMessageFragment :RecyclerAnkoFragment<HomeMessageEntity>(){
    companion object{ const val message = FragmentComponent.Config.fragment+"message" }

    override fun apiData(offset: Int, type: Int): Single<List<HomeMessageEntity>> {
        return Single.just(arrayListOf(
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity(),
            HomeMessageEntity()
        ))
    }
}