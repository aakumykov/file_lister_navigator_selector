package com.github.aakumykov.kotlin_playground

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class MainViewModel : BasicViewModel(), FileLister.ListCache, FileExplorer.PathCache {

    private var isRirstRun: Boolean = true

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer = LocalFileExplorer(initialPath, LocalFileLister(),
        this, this)

    private val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> = _currentList

    private val _currentPath: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> = _currentPath


    override fun getFileLister(): FileLister = fileExplorer

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