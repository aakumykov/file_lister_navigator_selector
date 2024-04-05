package com.github.aakumykov.file_lister_navigator_selector.fragments.local

import android.os.Environment
import com.github.aakumykov.file_lister_navigator_selector.common.BasicViewModel
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator.LocalFileExplorer

class LocalViewModel : BasicViewModel<SortingMode>() {

    private var isRirstRun: Boolean = true

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer<SortingMode> =
        LocalFileExplorer(
            localFileLister = LocalFileLister(""),
            initialPath = initialPath,
            isDirMode = true,
            listCache = this,
            pathCache = this)

//    private val fileExplorer: FileExplorer =


    override fun getFileExplorer(): FileExplorer<SortingMode> = fileExplorer


    override fun cacheList(list: List<FSItem>) {
        _currentList.value = list
    }

    override fun cachePath(path: String) {
        _currentPath.value = path
    }

    fun startWork() {
        if (isRirstRun) {
            isRirstRun = false
            fileExplorer.listCurrentPath()
        }
    }
}