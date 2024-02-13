package com.github.aakumykov.storage_access_helper.storage_access_helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class StorageAccessHelper (protected var resultCallback: ResultCallback) {

    abstract fun requestStorageAccess()
    abstract fun requestReadingAccess()
    abstract fun requestWritingAccess()

    abstract fun hasStorageReadingAccess(): Boolean
    abstract fun hasStorageWritingAccess(): Boolean
    abstract fun hasStorageFullAccess(): Boolean

    abstract fun openStorageAccessSettings()


    companion object {

        fun create(fragment: Fragment, resultCallback: ResultCallback): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragment, resultCallback)
                else -> StorageAccessHelperLegacyForFragment(fragment, resultCallback)
            }
        }

        fun create(activity: FragmentActivity, resultCallback: ResultCallback): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(activity, resultCallback)
                else -> StorageAccessHelperLegacyForActivity(activity, resultCallback)
            }
        }
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