package com.lifecycle.demo.ui.select

import com.lifecycle.demo.inject.bean.DateSerializer
import com.lifecycle.demo.ui.select.popup.SelectOption
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ExamParam(
    val search_key: String = "",
    val search_key_word: String = "",
    val patient_class: String = "",
    val service_sect_id: String = "",

    @Serializable(with = DateSerializer::class)
    val observation_start_date: Date = Date(),

    @Serializable(with = DateSerializer::class)
    val observation_end_date: Date = Date(),
    val page_size: Int = 20,
    val page_index: Int = 0,
    val request_dept: String = "",
    val dataSource: String = "",
    val is_reverse: Boolean = true
){
    constructor(checkList:List<SelectOption>) : this() {

    }
}