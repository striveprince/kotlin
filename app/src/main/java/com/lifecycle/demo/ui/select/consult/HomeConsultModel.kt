package com.lifecycle.demo.ui.select.consult

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.api
import com.lifecycle.demo.base.util.getString
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.inject.bean.CommonDataBean
import com.lifecycle.demo.ui.select.ExamParam
import com.lifecycle.demo.ui.select.popup.PopupSelect
import com.lifecycle.demo.ui.select.popup.SelectOption
import com.lifecycle.demo.ui.select.popup.SelectTitle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class HomeConsultModel: ListViewModel<ExamResultEntity>() {
    val params = MutableLiveData(ExamParam())
    val position = MutableLiveData(0)
    val date = MutableLiveData("一天内")
    val grid = MutableLiveData(false)
    val sort = MutableLiveData(false)
    val list = ArrayList<PopupSelect>()
    private val dataList = ArrayList<PopupSelect>()
    private val patientList = ArrayList<PopupSelect>()
    private val serviceList = ArrayList<PopupSelect>()
    private val dateList = ArrayList<PopupSelect>()

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        httpData = { o, s -> suspend {
            api.netApi.httpApi.examList(examParams(getOffset(o, s))) }.restful { it.result.toEntities() } }
        params.observer(owner) { start(AdapterType.refresh) }
        sort.observer(owner) { params.value = params(it, offset) }
        loadingState.observer(owner){ Timber.i("SmartRefreshState:$it,Original:${stateOriginal(it)} running ${isStateRunning(it)},end:${isStateEnd(it)},start:${isStateStart(it)}") }
    }

    fun params(it: Boolean = false, offset: Int = 0, list: List<PopupSelect> = ArrayList()) = (params.value ?: ExamParam()).apply {
        is_reverse = it
        page_index = offset
    }.run { if (list.isEmpty()) ExamParam().copy(this) else this.checkList(list) }

    private fun examParams(offset: Int) = (params.value ?: params(offset = offset)).apply { page_index = offset }


    private fun getOffset(offset: Int, state: Int): Int {
        return if (state.stateEqual(AdapterType.refresh)) 1 else offset
    }

    fun requireData(position: Int): Single<List<PopupSelect>> {
        return if (list.isEmpty()) api.netApi.httpApi.commonData().restful()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { convertList(position, it) }
            .flatMap { selectOption(position) }
        else selectOption(position)
    }


    fun arrayList(e: PopupSelect): ArrayList<PopupSelect> {
        return when (e.type) {
            getString(R.string.type_seek) -> dataList
            getString(R.string.type_inspect) -> patientList
            getString(R.string.type_system) -> serviceList
            else -> dateList
        }
    }

    private fun selectOption(position: Int): Single<List<PopupSelect>> {
        return when (position) {
            0 -> Single.just(dataList)
            1 -> Single.just(patientList)
            2 -> Single.just(dateList)
            else -> Single.just(list)
        }
    }

    private fun convertList(position: Int, it: CommonDataBean): Single<List<PopupSelect>> {
        addDataList(getString(R.string.type_seek), "data_source", it.data_source, dataList)
        addDataList(getString(R.string.type_inspect), "patient_class", it.patient_class, patientList)
        addDateList(getString(R.string.type_date))
        addDataList(getString(R.string.type_system), "service_sect_id", it.service_sect, serviceList)
        return selectOption(position)
    }

    private fun addDateList(string: String) {
        val dates = mutableListOf(
            SelectOption(string, getString(R.string.date_not_limit)),
            SelectOption(string, getString(R.string.day_one)).apply { checked.set(true) },
            SelectOption(string, getString(R.string.week_one)),
            SelectOption(string, getString(R.string.month_one)),
            SelectOption(string, getString(R.string.year_one))
        )
        dateList.addAll(dates)
        list.add(SelectTitle(string))
        list.addAll(dateList)
    }

    private fun addDataList(title: String, type: String, map: Map<String, String>, dataList: MutableList<PopupSelect>) {
        for (s in map.entries) dataList.add(SelectOption(type, s.value, s.key))
        if (dataList.isNotEmpty()) {
            list.add(SelectTitle(title))
            list.addAll(dataList)
        }
    }

}