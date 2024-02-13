package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class StorageAccessHelperLegacyForActivity (private val activity: FragmentActivity,
                                                               resultCallback: ResultCallback)
    : StorageAccessHelperLegacy(resultCallback) {


    override val fullStoragePermissionsRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            *fullAccessPermissions(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NEVER) }
        )
    }

    override val readingStoragePermissionRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            readAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NEVER) }
        )
    }

    override val writingStoragePermissionRequester: PermissionsRequester by lazy {
        activity.constructPermissionsRequest(
            writeAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NEVER) }
        )
    }


    override fun isPermissionGranted(permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity.checkCallingOrSelfPermission(permission)
    }

    override fun openStorageAccessSettings() {
        IntentHelper.appSettingsIntent(activity).also {
            activity.startActivity(it)
        }
    }
}