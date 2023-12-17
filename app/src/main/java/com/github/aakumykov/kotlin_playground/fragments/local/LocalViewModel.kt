package com.github.aakumykov.kotlin_playground.fragments.local

import android.os.Environment
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.fs_item.FSItem
import com.github.aakumykov.kotlin_playground.common.BasicViewModel
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class LocalViewModel : BasicViewModel() {

    private var isRirstRun: Boolean = true

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer =
        LocalFileExplorer(
            initialPath = initialPath,
            isDirMode = true,
            localFileLister = LocalFileLister(""),
            listCache = this,
            pathCache = this)

//    private val fileExplorer: FileExplorer =


    override fun getFileExplorer(): FileExplorer = fileExplorer


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