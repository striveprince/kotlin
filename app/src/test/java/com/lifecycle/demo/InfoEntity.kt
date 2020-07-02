package com.lifecycle.demo
data class InfoEntity<T>(val code: Int,val msg: String,val result: T)

data class Bean(
    val id:String,
    val issues:String,
    val master:String,
    val master_name:String,
    val max_number:Int,
    val observation_uid:String,
    val password:String
)
