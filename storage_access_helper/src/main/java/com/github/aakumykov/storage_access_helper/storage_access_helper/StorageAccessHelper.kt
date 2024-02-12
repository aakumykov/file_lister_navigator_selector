package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class StorageAccessHelper (protected var resultCallback: ResultCallback) {

    abstract fun requestStorageAccess()
    abstract fun requestReadingAccess()
    abstract fun requestWritingAccess()

    @Deprecated("Используй hasStorageReadingAccess(), hasStorageWritingAccess")
    abstract fun hasStorageAccess(): Boolean

    abstract fun hasStorageReadingAccess(): Boolean
    abstract fun hasStorageWritingAccess(): Boolean

    abstract fun openStorageAccessSettings()


    companion object {

        fun create(fragment: Fragment, resultCallback: ResultCallback): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragment, resultCallback)
                else -> throw Exception()/*StorageAccessHelperLegacy(fragment, resultCallback)*/
            }
        }

        fun create(componentActivity: FragmentActivity, resultCallback: ResultCallback): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(componentActivity, resultCallback)
                else -> StorageAccessHelperLegacy(componentActivity, resultCallback)
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    interface ResultCallback {
        fun onStorageAccessResult(grantedMode: StorageAccessMode)
    }

    enum class StorageAccessMode {
        READING_YES,
        READING_NO,
        READING_NEVER,
        WRITING_YES,
        WRITING_NO,
        WRITING_NEVER,
        FULL_YES,
        FULL_NO,
        FULL_NEVER
    }
}