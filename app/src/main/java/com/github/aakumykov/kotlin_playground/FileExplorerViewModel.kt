package com.github.aakumykov.kotlin_playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister

class FileExplorerViewModel : ViewModel(), FileExplorer.ListCache, FileExplorer.PathCache {

    private var _fileExplorer: FileExplorer? = null

    val fileExplorer get() = _fileExplorer!!

    fun setFileExplorer(fileExplorer: FileExplorer) {
        _fileExplorer = fileExplorer
    }


    private val _listMutableLiveData: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> get() = _listMutableLiveData


    private val _pathMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> get() = _pathMutableLiveData


    override fun cachePath(path: String) {
        _pathMutableLiveData.value = path
    }

    override fun cacheList(list: List<FSItem>) {
        _listMutableLiveData.value = list
    }
}