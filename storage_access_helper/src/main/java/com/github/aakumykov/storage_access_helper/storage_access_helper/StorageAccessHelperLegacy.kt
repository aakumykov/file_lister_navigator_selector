package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class StorageAccessHelperLegacy internal constructor(private val activity: FragmentActivity,
                                                     resultCallback: ResultCallback): StorageAccessHelper(resultCallback) {

    private val readingStoragePermissionsRequester: PermissionsRequester

    init {
        readingStoragePermissionsRequester = activity.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback.onStorageAccessResult(true) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(false) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(false) }
        )
    }

    override fun requestStorageAccess() {
        readingStoragePermissionsRequester.launch()
    }

    override fun hasStorageAccess(): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun hasStorageReadingAccess(): Boolean {
        return isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun hasStorageWritingAccess(): Boolean {
        return isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun openStorageAccessSettings() {
        IntentHelper.appSettingsIntent(activity)
    }


    private fun isPermissionGranted(usesPermissionString: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity.checkCallingOrSelfPermission(usesPermissionString)
    }
}