package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

class StorageAccessHelperModern internal constructor(private val activity: FragmentActivity,
                                                     resultCallback: ResultCallback): StorageAccessHelper(resultCallback) {

    // TODO: лениво
    private val activityResultLauncher: ActivityResultLauncher<Unit>

    init {
        activityResultLauncher = activity.registerForActivityResult(ManageAllFilesContract(activity.packageName)) { isGranted ->
            invokeOnResult(isGranted)
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
        if (hasStorageAccess())
            invokeOnResult(true)
        else
            activityResultLauncher.launch(Unit)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageAccess(): Boolean = hasStorageAccessModern()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageReadingAccess(): Boolean = hasStorageAccessModern()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasStorageWritingAccess(): Boolean = hasStorageAccessModern()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun openStorageAccessSettings() {
        activity.startActivity(IntentHelper.manageAllFilesIntent(activity))
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun hasStorageAccessModern(): Boolean = Environment.isExternalStorageManager()

    private fun invokeOnResult(isGranted: Boolean) {
        resultCallback.onStorageAccessResult(
            if (isGranted) StorageAccessMode.FULL_YES
            else StorageAccessMode.FULL_NO
        )
    }
}