package com.lifecycle.demo.inject.data.net.bean

import com.lifecycle.demo.inject.data.net.SignParams
import kotlinx.serialization.Serializable

@Serializable
data class TokenBean (
    var msg: String="",
    var code: Int=0,
    var token: String?="",
    var refresh_token: String?="",
    var expires_in: Long=0L,
    var refresh_expires_in: Long=0L,
    var token_type: String? = ""
) {
    fun toParams(): SignParams {
        return SignParams("", "")
    }
}
