package com.lifecycle.demo.ui.home.message

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.rx.adapter.life.diff.RecyclerDiffFragment
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import io.reactivex.Single


@Route(path = message)
class HomeMessageFragment : RecyclerDiffFragment<HomeMessageEntity>(){
    companion object{ const val message = "$home/message" }

    override fun apiData(offset: Int, state: Int): Single<List<HomeMessageEntity>> {
       return Single.just(arrayListOf(
            HomeMessageEntity(),
            HomeMessageEntity()
        ))
    }

}