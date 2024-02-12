package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.fragment.app.FragmentActivity

abstract class StorageAccessHelper (protected var resultCallback: ResultCallback) {

    abstract fun requestStorageAccess()

    @Deprecated("Используй hasStorageReadingAccess(), hasStorageWritingAccess")
    abstract fun hasStorageAccess(): Boolean

    abstract fun hasStorageReadingAccess(): Boolean
    abstract fun hasStorageWritingAccess(): Boolean

    abstract fun openStorageAccessSettings()


    companion object {
        fun create(componentActivity: FragmentActivity,
                   resultCallback: ResultCallback): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(componentActivity, resultCallback)
                else -> StorageAccessHelperLegacy(componentActivity, resultCallback)
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    interface ResultCallback {
        fun onStorageAccessResult(isGranted: Boolean)
    }
}