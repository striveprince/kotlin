package com.lifecycle.demo.ui.home.message

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.life.binding.data.recycler.diff.RecyclerFragment
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import io.reactivex.Single

@Route(path = message)
class HomeMessageFragment : RecyclerFragment<HomeMessageEntity>(){
    companion object{ const val message = FragmentComponent.Config.fragment+"message" }

    override fun apiData(offset: Int, state: Int): Single<List<HomeMessageEntity>> {
       return Single.just(arrayListOf(
            HomeMessageEntity(),
            HomeMessageEntity()
        ))
    }

}