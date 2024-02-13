package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class StorageAccessHelperLegacyForFragment (private val fragment: Fragment,
                                                               resultCallback: ResultCallback)
    : StorageAccessHelperLegacy(resultCallback) {

    override val fullStoragePermissionsRequester: PermissionsRequester by lazy {
        fragment.constructPermissionsRequest(
            *fullAccessPermissions(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.FULL_NEVER) }
        )
    }

    override val readingStoragePermissionRequester: PermissionsRequester by lazy {
        fragment.constructPermissionsRequest(
            readAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.READING_NEVER) }
        )
    }

    override val writingStoragePermissionRequester: PermissionsRequester by lazy {
        fragment.constructPermissionsRequest(
            writeAccessPermission(),
            requiresPermission = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_YES) },
            onPermissionDenied = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NO) },
            onNeverAskAgain = { resultCallback.onStorageAccessResult(StorageAccessMode.WRITING_NEVER) }
        )
    }


    override fun openStorageAccessSettings() {
        IntentHelper.appSettingsIntent(activity()).also {
            activity().startActivity(it)
        }
    }

    override fun isPermissionGranted(permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == fragment.requireActivity().checkCallingOrSelfPermission(permission)
    }


    private fun activity(): FragmentActivity = fragment.requireActivity()
}