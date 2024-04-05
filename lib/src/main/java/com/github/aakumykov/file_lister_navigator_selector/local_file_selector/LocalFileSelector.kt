package com.github.aakumykov.file_lister_navigator_selector.local_file_selector

import android.os.Bundle
import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator_dialog.LocalDirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

// TODO: SortingMode сделать параметром типа?
class LocalFileSelector: FileSelector<SortingMode>() {

    private var _fileExplorer: FileExplorer<SortingMode>? = null


    override fun fileExplorer(): FileExplorer<SortingMode> {
        if (null == _fileExplorer)
            _fileExplorer = createFileExplorer(arguments)
        return _fileExplorer!!
    }


    override fun defaultStartPath(): String = Environment.getExternalStorageDirectory().path


    private fun createFileExplorer(arguments: Bundle?): FileExplorer<SortingMode> {

        val initialPath = arguments?.getString(START_PATH) ?: Environment.getExternalStorageDirectory().path

        return LocalFileExplorer(
            initialPath = initialPath,
            localFileLister = LocalFileLister(""),
            localDirCreator = LocalDirCreator(),
            listCache = null,
            pathCache = null,
        )
    }

    override fun dirCreatorDialog(basePath: String): DirCreatorDialog {
        return LocalDirCreatorDialog.create(basePath)
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