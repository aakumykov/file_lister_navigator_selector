package com.github.aakumykov.kotlin_playground

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.yandex_disk_file_explorer.YandexDiskFileExplorer
import com.github.aakumykov.yandex_disk_file_lister.YandexDiskFileLister
import kotlin.concurrent.thread

class YandexViewModel : ViewModel(), FileExplorer.ListCache {

    private val handler: Handler = Handler(Looper.getMainLooper())

    private val fileExplorer: FileExplorer by lazy {
        YandexDiskFileExplorer(
            YandexDiskFileLister(YandexFragment.yandexAuthToken!!),
            listCache = this
        )
    }

    private val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> = _currentList

    override fun cacheList(list: List<FSItem>) {
        handler.post { _currentList.value = list }
    }
}