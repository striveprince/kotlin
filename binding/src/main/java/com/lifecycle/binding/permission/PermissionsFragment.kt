package com.lifecycle.binding.permission

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment


class PermissionsFragment : Fragment() {
    companion object{
        const val PERMISSIONS_REQUEST_CODE = 42
    }
    val permissionMap = HashMap<String, Permission>()

    var collector : (HashMap<String, Permission>)->Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(@NonNull permissions: Array<String>): PermissionsFragment {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
        return this
    }

    fun addCollector(block:(HashMap<String, Permission>)->Unit = {}){
        collector = block
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    private fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {
        permissions.forEachIndexed { i, s ->
            val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
            permissionMap[s] = Permission(s, granted, shouldShowRequestPermissionRationale[i])
            collector(permissionMap)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun isGranted(permission: String?): Boolean {
        return activity!!.checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun isRevoked(permission: String): Boolean {
        return activity?.let { it.packageManager.isPermissionRevokedByPolicy(permission, it.packageName) } ?: false
    }

}


