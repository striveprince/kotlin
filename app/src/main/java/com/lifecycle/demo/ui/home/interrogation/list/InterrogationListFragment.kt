package com.lifecycle.demo.ui.home.interrogation.list

import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.InterrogationParams
import com.lifecycle.demo.inject.data.net.bean.InterrogationDataBean
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.rxlist.InterrogationListFragment.Companion.interrogationList
import com.lifecycle.binding.Constant
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.coroutines.adapter.life.diff.RecyclerDiffFragment
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.demo.base.util.*
import com.lifecycle.demo.inject.ApiException
import com.lifecycle.demo.inject.data.net.bean.InterrogationBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//@Route(path = interrogationList)
class InterrogationListFragment : RecyclerDiffFragment<Diff>() {

    companion object {
        const val interrogationList = "$interrogation/list"
    }

    override suspend fun require(startOffset: Int, state: Int): Flow<List<InterrogationListEntity>> {
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


