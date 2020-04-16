package com.lifecycle.demo.ui.select.fragment

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*
import com.lifecycle.coroutines.adapter.life.RecyclerFragment
import com.lifecycle.demo.base.util.api
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.ui.select.ExamParam
import com.lifecycle.demo.ui.select.SelectActivity.Companion.select
import com.lifecycle.demo.ui.select.SelectModel
import com.lifecycle.demo.ui.select.fragment.HomeListFragment.Companion.homeList
import kotlinx.coroutines.flow.Flow

@Route(path = homeList)
class HomeListFragment: RecyclerFragment<ExamResultEntity>(){
    companion object{ const val homeList = "$select/list" }
    private var examParam = ExamParam()

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        requireActivity().run {
            viewModel<SelectModel>().params.observer(this){
                model.loadingState.run { if (isStateEnd(value?:0)) value = stateStart(AdapterType.refresh) }
                examParam = it
            }
        }
    }

    override suspend fun require(startOffset: Int, state: Int): Flow<List<ExamResultEntity>> {
        return suspend { api.netApi.httpApi.examList(examParam) }.restful{ it.result.toEntities() }
    }


}