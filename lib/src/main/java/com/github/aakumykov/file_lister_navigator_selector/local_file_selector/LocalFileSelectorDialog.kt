package com.github.aakumykov.file_lister_navigator_selector.local_file_selector

import android.os.Bundle
import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelectorDialog
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalFileSelectorDialog private constructor(): FileSelectorDialog() {

    private var _fileExplorer: FileExplorer? = null


    override fun fileExplorer(): FileExplorer {

        if (null == _fileExplorer)
            _fileExplorer = createFileExplorer(arguments)

        return _fileExplorer!!
    }


    override fun defaultStartPath(): String
        = Environment.getExternalStorageDirectory().path


    private fun createFileExplorer(arguments: Bundle?): FileExplorer {

        val initialPath = arguments?.getString(START_PATH) ?: Environment.getExternalStorageDirectory().path

        return LocalFileExplorer(
            initialPath = initialPath,
            localFileLister = LocalFileLister(
                ""
            ),
            listCache = null,
            pathCache = null,
        )
    }

    companion object {
        val TAG: String = LocalFileSelectorDialog::class.java.simpleName

        fun create(callback: Callback,
                   startPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false) : LocalFileSelectorDialog
        {
            return LocalFileSelectorDialog().apply {
                setCallback(callback)
                arguments = Bundle().apply {
                    putString(START_PATH, startPath)
                    putBoolean(IS_DIR_MODE, isDirMode)
                    putBoolean(IS_MULTIPLE_SELECTION_MODE, isMultipleSelectionMode)
                }
            }
        }
    }
}