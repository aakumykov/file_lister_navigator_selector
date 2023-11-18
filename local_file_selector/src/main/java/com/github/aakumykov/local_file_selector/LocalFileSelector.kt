package com.github.aakumykov.local_file_selector

import android.os.Bundle
import android.os.Environment
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_selector.FileSelector
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class LocalFileSelector : FileSelector() {

    override fun fileExplorer(): FileExplorer {

        /*var initialPath = arguments?.getString(INITIAL_PATH)

        if (null == initialPath)
            initialPath = Environment.getExternalStorageDirectory().path*/

        // TODO: проверить, если не будет INITIAL_PATH
        val initialPath = arguments?.getString(INITIAL_PATH) ?: Environment.getExternalStorageDirectory().path

        return LocalFileExplorer(
            initialPath = initialPath,
            localFileLister = LocalFileLister(),
            listCache = null,
            pathCache = null,
        )
    }

    companion object {
        fun create(initialPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false) : LocalFileSelector
        {
            return LocalFileSelector().apply {
                arguments = Bundle().apply {
                    putString(INITIAL_PATH, initialPath)
                    putBoolean(IS_DIR_MODE, isDirMode)
                    putBoolean(IS_MULTIPLE_SELECTION_MODE, isMultipleSelectionMode)
                }
            }
        }
    }
}