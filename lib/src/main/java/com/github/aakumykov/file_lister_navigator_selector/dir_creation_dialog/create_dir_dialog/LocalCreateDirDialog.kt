package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog

import android.os.Environment
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.LocalDirCreator

class LocalCreateDirDialog: CreateDirDialog() {

    companion object {
        val TAG: String = LocalCreateDirDialog::class.java.name
        fun create(): LocalCreateDirDialog = LocalCreateDirDialog()
    }

    override fun basePath(): String = Environment.getExternalStorageDirectory().absolutePath

    override fun cloudDirCreator(): CloudDirCreator = LocalDirCreator()
}