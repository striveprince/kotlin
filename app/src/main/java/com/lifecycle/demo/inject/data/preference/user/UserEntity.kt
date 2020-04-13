package com.lifecycle.demo.inject.data.preference.user

import com.lifecycle.binding.life.AppLifecycle.Companion.application
import com.lifecycle.binding.util.application
import com.lifecycle.binding.util.sharedPreferences
import com.lifecycle.binding.util.vetoable
import com.lifecycle.demo.inject.data.net.SignParams
import kotlinx.serialization.Serializable


class UserEntity {
    fun toParams(): SignParams {
        return SignParams("","'")
    }

    private val sharedPreferences = application.application().sharedPreferences("user")
    var msg: String by sharedPreferences.vetoable("")
    var code: Int by sharedPreferences.vetoable(0)
    var token: String by sharedPreferences.vetoable("")
    var refresh_token: String by sharedPreferences.vetoable("")
    var expires_in: Long by sharedPreferences.vetoable(0L)
    var refresh_expires_in: Long by sharedPreferences.vetoable(0L)
    var token_type: String by sharedPreferences.vetoable("")
}