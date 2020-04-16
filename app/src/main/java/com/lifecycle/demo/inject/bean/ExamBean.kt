package com.lifecycle.demo.inject.bean

import kotlinx.serialization.Serializable

@Serializable
data class ExamBean(
    val code: Int,
    val count: Int,
    val msg: String,
    val page_index: Int,
    val page_size: Int,
    val result: List<ExamResultBean>
)

@Serializable
data class ExamResultBean(
    val accession_number: String,
    val name: String,
    val observation_date: String,
    val procedure_name: String,
    val service_sect_id: String
)