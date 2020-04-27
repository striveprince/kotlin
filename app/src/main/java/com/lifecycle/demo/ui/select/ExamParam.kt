package com.lifecycle.demo.ui.select

import com.lifecycle.demo.inject.bean.DateSerializer
import com.lifecycle.demo.ui.select.popup.PopupSelect
import com.lifecycle.demo.ui.select.popup.SelectOption
import kotlinx.serialization.Serializable
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
) {
    constructor(checkList: List<PopupSelect>) : this() {

    }
}