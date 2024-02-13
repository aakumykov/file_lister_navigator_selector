package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

abstract class StorageSettingsHelper(private val activity: FragmentActivity) {

    abstract fun openStorageAccessSettings()

    fun openStoragePermissions() {
        activity.startActivity(IntentHelper.appSettingsIntent(activity))
    }

    companion object {
        fun create(activity: FragmentActivity): StorageSettingsHelper {
            return when (isAndroidROrLater()) {
                true -> StorageSettingsHelperModern(activity)
                false -> StorageSettingsHelperLegacy(activity)
            }
        }
    }
}

class StorageSettingsHelperLegacy(private val activity: FragmentActivity) : StorageSettingsHelper(activity) {
    override fun openStorageAccessSettings() {
        openStoragePermissions()
    }
}

class StorageSettingsHelperModern(private val activity: FragmentActivity) : StorageSettingsHelper(activity) {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun openStorageAccessSettings() {
        activity.startActivity(IntentHelper.manageAllFilesIntent(activity))
    }
}