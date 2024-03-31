package com.github.aakumykov.file_lister_navigator_selector.LocalFileSelector2

import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.file_selector2.FileSelector2
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalFileSelector2 : FileSelector2() {

    companion object {
        val TAG: String = LocalFileSelector2::class.java.simpleName
    }

    override fun fileExplorer(): FileExplorer {
        return LocalFileExplorer(
            initialPath = Environment.getExternalStorageDirectory().absolutePath,
            isDirMode = false,
            LocalFileLister(""),
            LocalDirCreator(),
            null,
            null
        )
    }

    /*override fun sortingModeDialog(): AlertDialog {

    }

    override fun sortingModeSelectionListener(): DialogInterface.OnClickListener {

    }*/
}