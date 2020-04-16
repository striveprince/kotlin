package com.lifecycle.demo.inject.bean

import kotlinx.serialization.Serializable

@Serializable
data class CommonDataBean(
    val code: Int,
    val data_source: Map<String, String>,
    val msg: String,
    val patient_class: Map<String, String>,
    val request_dept: Map<String, String>,
    val service_sect: Map<String, String>,
    val sreach_keys: SreachKeysBean
)

@Serializable
data class SreachKeysBean(
    val AccessionNumber: String,
    val HealthCardNO: String,
    val MedRecNO: String,
    val Name: String,
    val PatientID: String
)
