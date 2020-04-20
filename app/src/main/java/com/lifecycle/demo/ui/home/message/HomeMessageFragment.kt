package com.lifecycle.demo.ui.home.message

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.base.recycler.diff.RecyclerDiffFragment
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@Route(path = message)
class HomeMessageFragment : RecyclerDiffFragment<HomeMessageEntity>(){
    companion object{ const val message = "$home/message" }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.httpData = {offset,state-> apiData(offset,state)}
    }

    fun apiData(offset: Int, state: Int): Flow<List<HomeMessageEntity>> {
       return flow{ arrayListOf(
           HomeMessageEntity(),
           HomeMessageEntity()
       ) }
    }

}