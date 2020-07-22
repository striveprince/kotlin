package com.lifecycle.demo.ui.select

import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.copy
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.bean.DateSerializer
import com.lifecycle.demo.inject.data.net.converter.ApiParams
import com.lifecycle.demo.ui.select.popup.PopupSelect
import com.lifecycle.demo.ui.select.popup.SelectOption
import kotlinx.serialization.Serializable
import java.lang.StringBuilder
import java.util.*

@Serializable
data class ExamParam(
    var search_key: String = "",
    var search_key_word: String = "",
    var patient_class: String = "",
    var service_sect_id: String = "",

    @Serializable(with = DateSerializer::class)
    var observation_start_date: Date = Date(),

    @Serializable(with = DateSerializer::class)
    var observation_end_date: Date = Date(),
    var page_size: Int = 20,
    var page_index: Int = 0,
    var request_dept: String = "",
    var dataSource: String = "",
    var is_reverse: Boolean = false
) : ApiParams {
    fun checkList(list: List<PopupSelect>): ExamParam {
        val examParam = ExamParam().copy(this)
        val patientClassSet = examParam.patient_class.toOptionSet()
        val sectIdSet = examParam.service_sect_id.toOptionSet()
        val dataSourceSet = examParam.dataSource.toOptionSet()
        observation_end_date = Date()
        for (popupSelect in list) {
            when (popupSelect.type) {
                "patient_class" -> patientClassSet.add(popupSelect.option)
                "service_sect_id" -> sectIdSet.add(popupSelect.option)
                "data_source" -> dataSourceSet.add(popupSelect.option)
                AppLifecycle.application.getString(R.string.type_date)->examParam.observation_start_date = popupSelect.time()
            }
        }
        examParam.patient_class = patientClassSet.toParams()
        examParam.service_sect_id = sectIdSet.toParams()
        examParam.dataSource = dataSourceSet.toParams()
        return examParam
    }

    private fun Set<String>.toParams(): String {
        val builder = StringBuilder()
        for (any in this) if(any.isNotEmpty())builder.append(any).append(",")
        if(builder.isNotEmpty())builder.deleteCharAt(builder.lastIndex)
        return builder.toString()
    }

    private fun String.toOptionSet(): HashSet<String> {
        val set = HashSet<String>()
        val list = this.split(",")
        for (s in list) set.add(s)
        return set
    }
}