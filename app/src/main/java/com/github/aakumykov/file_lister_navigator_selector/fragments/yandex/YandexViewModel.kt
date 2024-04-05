package com.github.aakumykov.file_lister_navigator_selector.fragments.yandex

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer

class YandexViewModel : ViewModel(), FileExplorer.ListCache, FileExplorer.PathCache {

    private val handler = Handler(Looper.getMainLooper())


    private var _fileExplorer: FileExplorer<SortingMode>? = null

    val fileExplorer get() = _fileExplorer!!

    fun setFileExplorer(fileExplorer: FileExplorer<SortingMode>) {
        _fileExplorer = fileExplorer
    }


    private val _listMutableLiveData: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> get() = _listMutableLiveData


    private val _pathMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String> get() = _pathMutableLiveData


    override fun cachePath(path: String) {
        handler.post { _pathMutableLiveData.value = path }
    }


    override fun cacheList(list: List<FSItem>) {
        handler.post { _listMutableLiveData.value = list }
    }
}