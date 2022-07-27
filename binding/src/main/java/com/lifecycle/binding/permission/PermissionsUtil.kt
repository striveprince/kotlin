package com.lifecycle.binding.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lifecycle.binding.life.AppLifecycle
import java.util.*

class PermissionsUtil(activity: FragmentActivity = AppLifecycle.activity() as FragmentActivity) {
    companion object { const val TAG = "PermissionsUtil" }

    private val permissionsFragment by lazy { getRxPermissionsFragment(activity) }

    private fun getRxPermissionsFragment(activity: FragmentActivity): PermissionsFragment {
        val permissionsFragment = findPermissionsFragment(activity)
        return permissionsFragment ?: with(activity) {
            PermissionsFragment().apply {
                supportFragmentManager.beginTransaction()
                    .add(this, TAG)
                    .commitAllowingStateLoss()
                supportFragmentManager.executePendingTransactions()
            }
        }
    }

    private fun findPermissionsFragment(activity: FragmentActivity): PermissionsFragment? {
        return activity.supportFragmentManager.findFragmentByTag(TAG) as? PermissionsFragment
    }

    fun request(vararg permissions: String,block :(Boolean)->Unit ){
        val unrequestedPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (isGranted(permission)||isRevoked(permission)) continue
            if (permissionsFragment.permissionMap[permission] == null) unrequestedPermissions.add(permission)
        }
        permissionsFragment.requestPermissions(unrequestedPermissions.toTypedArray())
            .addCollector { it ->
                var b = true
                it.values.forEach { b = b and it.granted }
                block(b)
            }
    }

    private fun isGranted(permission: String): Boolean {
        return !isMarshmallow() || permissionsFragment.isGranted(permission)
    }

    private fun isRevoked(permission: String): Boolean {
        return isMarshmallow() && permissionsFragment.isRevoked(permission)
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    fun shouldShowRequestPermissionRationaleImplementation(activity: Activity, vararg permissions: String): Boolean {
//        for (p in permissions) {
//            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
//                return false
//            }
//        }
//        return true
//    }
}

fun Fragment.requirePermission(vararg permission: String, block :(Boolean)->Unit){
    val s = requireActivity().checkPermissions(*permission)
    if(s)block(s)
    else PermissionsUtil(requireActivity()).request(*permission){ block(it) }
}

fun FragmentActivity.requirePermission(vararg permission: String,block :(Boolean)->Unit){
    val s = checkPermissions(*permission)
    if(s)block(s)
    else PermissionsUtil(this).request(*permission){ block(it) }
}

@Suppress("DEPRECATED_IDENTITY_EQUALS")
fun Context.checkPermissions(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}


