package com.lifecycle.binding.permission

data class Permission(val name:String,val granted:Boolean,val shouldShowRequestPermissionRationale:Boolean = false)