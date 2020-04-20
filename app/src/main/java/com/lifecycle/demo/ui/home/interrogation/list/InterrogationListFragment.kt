package com.lifecycle.demo.ui.home.interrogation.list

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.Constant
import com.lifecycle.binding.util.toEntities
import com.lifecycle.binding.util.viewModel
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.demo.base.recycler.diff.RecyclerDiffFragment
import com.lifecycle.demo.base.util.api
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.inject.Api
import com.lifecycle.demo.inject.data.net.InterrogationParams
import com.lifecycle.demo.inject.data.net.bean.InterrogationDataBean
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation
import com.lifecycle.demo.ui.home.interrogation.InterrogationListEntity
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment.Companion.interrogationList
import kotlinx.coroutines.flow.Flow

@Route(path = interrogationList)
class InterrogationListFragment : RecyclerDiffFragment<InterrogationListEntity>() {

    companion object {
        const val interrogationList = "$interrogation/list"
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.httpData= {start, state -> require(start,state) }
    }

    private suspend fun require(startOffset: Int, state: Int): Flow<List<InterrogationListEntity>> {
        val taskCategory = when (arguments?.getString(Constant.params)) {
            "new" -> 1
            "wait" -> 2
            else -> 0
        }
        return api.getInterrogationList(taskCategory, startOffset, state)
    }

    private suspend fun Api.getInterrogationList(taskCategory: Int, position: Int, state: Int): Flow<List<InterrogationListEntity>> {
        val params = InterrogationParams(taskCategory, position)
        return suspend{ netApi.httpApi.getInterrogationList(params) }
            .restful {
                notifyCount(taskCategory,it)
                it.result.toEntities()
            }
    }

    private fun notifyCount(taskCategory: Int, it: InterrogationDataBean) {
        launchUI {
            (activity as FragmentActivity).viewModel<HomeModel>().apply {
                when (taskCategory) {
                    0 -> allCount.value = it.count
                    1 -> newCount.value = it.count
                    2 -> waitCount.value = it.count
                }
            }
        }
    }


}


