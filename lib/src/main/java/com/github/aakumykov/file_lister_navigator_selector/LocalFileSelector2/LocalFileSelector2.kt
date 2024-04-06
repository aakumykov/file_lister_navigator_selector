package com.github.aakumykov.file_lister_navigator_selector.LocalFileSelector2

import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.file_selector2.FileSelector2
import com.github.aakumykov.file_lister_navigator_selector.file_selector2.SortingModesMap
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator_dialog.LocalDirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalFileSelector2 : FileSelector2<SortingMode>() {

    companion object {
        val TAG: String = LocalFileSelector2::class.java.simpleName
    }

    override fun fileExplorer(): FileExplorer<SortingMode> {
        return LocalFileExplorer(
            localFileLister = LocalFileLister(""),
            localDirCreator = LocalDirCreator(),
            initialPath = Environment.getExternalStorageDirectory().absolutePath,
            isDirMode = false,
            defaultSortingMode = SortingMode.NAME_DIRECT
        )
    }

    override fun  dirCreatorDialog(basePath: String): DirCreatorDialog {
        return LocalDirCreatorDialog.create(basePath)
    }

    override fun getSortingModesMap(): SortingModesMap {
        return mapOf(
            SortingMode.NAME_DIRECT.name to R.string.sorting_mode_name_direct,
            SortingMode.NAME_REVERSE.name to R.string.sorting_mode_name_reverse,

            SortingMode.C_TIME_DIRECT.name to R.string.sorting_mode_c_time_direct,
            SortingMode.C_TIME_REVERSE.name to R.string.sorting_mode_c_time_reverse,

            SortingMode.M_TIME_DIRECT.name to R.string.sorting_mode_m_time_direct,
            SortingMode.M_TIME_REVERSE.name to R.string.sorting_mode_m_time_reverse,

            SortingMode.SIZE_DIRECT.name to R.string.sorting_mode_size_direct,
            SortingMode.SIZE_REVERSE.name to R.string.sorting_mode_size_reverse,
        )
    }

    override fun sortingNameToSortingMode(name: String): SortingMode {
        return SortingMode.valueOf(name)
    }
}