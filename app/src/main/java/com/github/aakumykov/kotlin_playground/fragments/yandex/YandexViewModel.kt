package com.github.aakumykov.kotlin_playground.fragments.yandex

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem

class YandexViewModel : ViewModel(), FileExplorer.ListCache, FileExplorer.PathCache {

    private val handler = Handler(Looper.getMainLooper())


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
        handler.post { _pathMutableLiveData.value = path }
    }


    override fun cacheList(list: List<FSItem>) {
        handler.post { _listMutableLiveData.value = list }
    }
}