package com.github.aakumykov.file_lister_navigator_selector.local_file_selector

import android.os.Bundle
import android.os.Environment

class LocalFileSelector private constructor(): com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector() {

    private var _fileExplorer: com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer? = null


    override fun fileExplorer(): com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer {

        if (null == _fileExplorer)
            _fileExplorer = createFileExplorer(arguments)

        return _fileExplorer!!
    }


    override fun defaultStartPath(): String
        = Environment.getExternalStorageDirectory().path


    private fun createFileExplorer(arguments: Bundle?): com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer {

        val initialPath = arguments?.getString(START_PATH) ?: Environment.getExternalStorageDirectory().path

        return com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer(
            initialPath = initialPath,
            localFileLister = com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister(
                ""
            ),
            listCache = null,
            pathCache = null,
        )
    }

    companion object {
        val TAG: String = LocalFileSelector::class.java.simpleName

        fun create(callback: Callback,
                   startPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false) : LocalFileSelector
        {
            return LocalFileSelector().apply {
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