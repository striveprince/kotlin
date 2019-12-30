package com.lifecycle.demo.ui.home.interrogation.list

import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.base.util.launchMain
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.base.util.toEntities
import com.lifecycle.demo.base.util.viewModel
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.InterrogationParams
import com.lifecycle.demo.inject.data.net.bean.InterrogationDataBean
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment.Companion.interrogationList
import com.lifecycle.binding.Constant
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.rx.adapter.life.diff.RxRecyclerDiffFragment
import com.lifecycle.demo.ui.DemoApplication.Companion.api
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation
import io.reactivex.Single

@Route(path = interrogationList)
class InterrogationListFragment : RxRecyclerDiffFragment<Diff>() {

    companion object {
        const val interrogationList = "$interrogation/list"
    }

    override fun apiData(offset: Int, state: Int): Single<List<Diff>> {
        val taskCategory = when (arguments?.getString(Constant.params)) {
            "new" -> 1
            "wait" -> 2
            else -> 0
        }
        return api.getInterrogationList(taskCategory, offset, state)
    }

    private fun Api.getInterrogationList(taskCategory: Int, position: Int, state: Int): Single<List<Diff>> {
        val params = InterrogationParams(taskCategory, position)
        return if (taskCategory == 0) netApi.httpApi.getInterrogationList(params).restful()
            .doOnSuccess { notifyCount(taskCategory, it) }
            .map { it.result.toEntities<InterrogationListEntity>() }
        else Single.just(ArrayList())
    }

    private fun notifyCount(taskCategory: Int, it: InterrogationDataBean) {
        launchMain {
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