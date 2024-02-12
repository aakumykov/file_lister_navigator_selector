package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import androidx.fragment.app.FragmentActivity

interface StorageAccessHelper {

    fun requestStorageAccess(resultCallback: (isGranted: Boolean) -> Unit)

    @Deprecated("Используй hasStorageReadingAccess(), hasStorageWritingAccess")
    fun hasStorageAccess(): Boolean

    fun hasStorageReadingAccess(): Boolean
    fun hasStorageWritingAccess(): Boolean

    fun openStorageAccessSettings()


    companion object {
        fun create(componentActivity: FragmentActivity): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(componentActivity)
                else -> StorageAccessHelperLegacy(componentActivity)
            }
        }

        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
}