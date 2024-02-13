package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class StorageAccessHelperModern private constructor(private val activity: FragmentActivity? = null,
                                                     private val fragment: Fragment? = null,
                                                     resultCallback: ResultCallback): StorageAccessHelper(resultCallback) {

    internal constructor(activity: FragmentActivity, resultCallback: ResultCallback): this(activity, null, resultCallback)

    internal constructor(fragment: Fragment, resultCallback: ResultCallback): this(null, fragment, resultCallback)


    // TODO: лениво
    private val activityResultLauncher: ActivityResultLauncher<Unit>

    init {
        activityResultLauncher = when {
            null != activity -> activity.registerForActivityResult(ManageAllFilesContract(activity.packageName)) { isGranted -> invokeOnResult(isGranted) }
            null != fragment -> fragment.registerForActivityResult(ManageAllFilesContract(fragment.requireActivity().packageName)) { isGranted -> invokeOnResult(isGranted) }
            else -> throw noActivityAndFragmentException()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestReadingAccess() {
        requestStorageAccess()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestWritingAccess() {
        requestStorageAccess()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestStorageAccess() {
        if (hasStorageFullAccess())
            invokeOnResult(true)
        else
            activityResultLauncher.launch(Unit)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageFullAccess(): Boolean = hasStorageAccessModern()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageReadingAccess(): Boolean = hasStorageAccessModern()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageWritingAccess(): Boolean = hasStorageAccessModern()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun openStorageAccessSettings() {
        activity().startActivity(IntentHelper.manageAllFilesIntent(context()))
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun hasStorageAccessModern(): Boolean = Environment.isExternalStorageManager()

    private fun invokeOnResult(isGranted: Boolean) {
        resultCallback.onStorageAccessResult(
            if (isGranted) StorageAccessMode.FULL_YES
            else StorageAccessMode.FULL_NO
        )
    }

    private fun noActivityAndFragmentException(): Exception {
        return IllegalStateException("Оба поля: activity и fragment равны null, а такого быть не должно!")
    }

    private fun context(): Context {
        return when {
            null != activity -> activity
            null != fragment -> fragment.requireContext()
            else -> throw noActivityAndFragmentException()
        }
    }

    private fun activity(): Activity {
        return when {
            null != activity -> activity
            null != fragment -> fragment.requireActivity()
            else -> throw noActivityAndFragmentException()
        }
    }
}