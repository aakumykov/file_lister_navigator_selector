package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator_dialog

import android.os.Environment
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.LocalDirCreator

class LocalDirCreatorDialog : DirCreatorDialog() {

    companion object {
        val TAG: String = LocalDirCreatorDialog::class.java.name
        fun create(): LocalDirCreatorDialog = LocalDirCreatorDialog()
    }

    override fun basePath(): String = Environment.getExternalStorageDirectory().absolutePath

    override fun cloudDirCreator(): CloudDirCreator = LocalDirCreator()
}