package com.github.aakumykov.file_lister_navigator_selector.local_file_selector

import android.os.Environment
import androidx.core.os.bundleOf
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SimpleSortingModeTranslator
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SortingModeTranslator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator_dialog.LocalDirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalFileSelector : FileSelector<SimpleSortingMode>() {

    override fun defaultSortingMode(): SimpleSortingMode = SimpleSortingMode.NAME

    override fun defaultReverseMode(): Boolean = false


    override fun fileExplorer(): FileExplorer<SimpleSortingMode> {
        return LocalFileExplorer(
            localFileLister = LocalFileLister(""),
            localDirCreator = LocalDirCreator(),
            initialPath = initialPath(),
            isDirMode = false,
            defaultSortingMode = defaultSortingMode()
        )
    }

    override fun defaultInitialPath(): String = Environment.getExternalStorageDirectory().absolutePath

    override fun  dirCreatorDialog(basePath: String): DirCreatorDialog {
        return LocalDirCreatorDialog.create(basePath)
    }

    override fun sortingModeTranslator(): SortingModeTranslator<SimpleSortingMode> {
        return SimpleSortingModeTranslator(resources)
    }


    companion object {
        fun create(initialPath: String): LocalFileSelector {
            return LocalFileSelector().apply {
                arguments = bundleOf(INITIAL_PATH to initialPath)
            }
        }

        val TAG: String = LocalFileSelector::class.java.simpleName
    }
}