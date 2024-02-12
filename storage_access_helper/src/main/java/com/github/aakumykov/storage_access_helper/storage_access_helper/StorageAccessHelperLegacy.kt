package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class StorageAccessHelperLegacy internal constructor(private val activity: FragmentActivity,
                                                     resultCallback: ResultCallback): StorageAccessHelper(resultCallback) {

    private val fullStoragePermissionsRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            *fullAccessPermissions(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NEVER) }
        )
    }

    private val readingStoragePermissionRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            readAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NEVER) }
        )
    }

    private val writingStoragePermissionRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            writeAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NEVER) }
        )
    }


    override fun requestReadingAccess() {
        readingStoragePermissionRequester.launch()
    }

    override fun requestWritingAccess() {
        writingStoragePermissionRequester.launch()
    }

    override fun requestStorageAccess() {
        fullStoragePermissionsRequester.launch()
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

    private fun fullAccessPermissions(): Array<String> = arrayOf(
        readAccessPermission(),
        writeAccessPermission()
    )

    private fun readAccessPermission(): String = android.Manifest.permission.READ_EXTERNAL_STORAGE

    private fun writeAccessPermission(): String = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
}