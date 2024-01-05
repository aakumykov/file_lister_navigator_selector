package com.github.aakumykov.file_lister_navigator_selector.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer

abstract class BasicViewModel : ViewModel(), FileExplorer.ListCache, FileExplorer.PathCache {

    protected val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> = _currentList

    protected val _currentPath: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> = _currentPath


    abstract fun getFileExplorer(): FileExplorer
}