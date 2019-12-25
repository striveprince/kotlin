package com.lifecycle.demo.ui.home.interrogation.list

import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.base.util.launchMain
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.base.util.toEntities
import com.lifecycle.demo.base.util.viewModel
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.net.InterrogationParams
import com.lifecycle.demo.inject.data.net.bean.InterrogationDataBean
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment.Companion.interrogationList
import com.lifecycle.binding.Constant
import com.lifecycle.binding.life.binding.data.recycler.diff.RecyclerFragment
import com.lifecycle.demo.ui.DemoApplication.Companion.api
import io.reactivex.Single

@Route(path = interrogationList)
class InterrogationListFragment : RecyclerFragment<InterrogationListEntity>() {


    companion object {
        const val interrogationList = FragmentComponent.Config.fragment + "interrogationList"
    }

    override fun apiData(offset: Int, type: Int): Single<List<InterrogationListEntity>> {
        val taskCategory = when (arguments?.getString(Constant.params)) {
            "new" -> 1
            "wait" -> 2
            else -> 0
        }
        return api.getInterrogationList(taskCategory, offset, type)
    }

    private fun Api.getInterrogationList(taskCategory: Int, position: Int, state: Int): Single<List<InterrogationListEntity>> {
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