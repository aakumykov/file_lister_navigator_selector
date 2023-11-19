package com.github.aakumykov.local_file_selector

import android.os.Bundle
import android.os.Environment
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_selector.FileSelector
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class LocalFileSelector : FileSelector() {

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
            localFileLister = LocalFileLister(),
            listCache = null,
            pathCache = null,
        )
    }

    companion object {
        fun create(startPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false) : LocalFileSelector
        {
            return LocalFileSelector().apply {
                arguments = Bundle().apply {
                    putString(START_PATH, startPath)
                    putBoolean(IS_DIR_MODE, isDirMode)
                    putBoolean(IS_MULTIPLE_SELECTION_MODE, isMultipleSelectionMode)
                }
            }
        }
    }
}