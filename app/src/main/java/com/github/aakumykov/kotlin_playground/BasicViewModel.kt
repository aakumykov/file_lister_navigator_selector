package com.github.aakumykov.kotlin_playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister

abstract class BasicViewModel : ViewModel(), FileLister.ListCache, FileExplorer.PathCache {

    protected val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> = _currentList

    protected val _currentPath: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> = _currentPath


    abstract fun getFileExplorer(): FileExplorer
}