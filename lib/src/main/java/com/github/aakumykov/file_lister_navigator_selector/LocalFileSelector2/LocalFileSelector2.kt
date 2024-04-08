package com.github.aakumykov.file_lister_navigator_selector.LocalFileSelector2

import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.file_selector2.FileSelector2
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SimpleSortingModeTranslator
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SortingModeTranslator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator_dialog.LocalDirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalFileSelector2 : FileSelector2<SortingMode>() {

    companion object {
        val TAG: String = LocalFileSelector2::class.java.simpleName
    }


    override fun defaultSortingMode(): SortingMode = SortingMode.NAME_DIRECT


    override fun fileExplorer(): FileExplorer<SortingMode> {
        return LocalFileExplorer(
            localFileLister = LocalFileLister(""),
            localDirCreator = LocalDirCreator(),
            initialPath = Environment.getExternalStorageDirectory().absolutePath,
            isDirMode = false,
            defaultSortingMode = defaultSortingMode()
        )
    }

    override fun  dirCreatorDialog(basePath: String): DirCreatorDialog {
        return LocalDirCreatorDialog.create(basePath)
    }

    override fun sortingModeTranslator(): SortingModeTranslator<SortingMode> {
        return SimpleSortingModeTranslator(resources)
    }
}