package com.github.aakumykov.file_lister_navigator_selector.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer

abstract class BasicViewModel : ViewModel(), FileExplorer.ListCache, FileExplorer.PathCache {

    protected val _currentSortingMode: MutableLiveData<FileSortingMode> = MutableLiveData(DEFAULT_SORTING_MODE)
    val sortingModeLiveData: LiveData<FileSortingMode> = _currentSortingMode


    protected val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val listLiveData: LiveData<List<FSItem>> = _currentList


    protected val _currentPath: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> = _currentPath


    abstract fun getFileExplorer(): FileExplorer


    companion object {
        val DEFAULT_SORTING_MODE = FileSortingMode.NAME_DIRECT
    }
}