package com.github.aakumykov.kotlin_playground

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class MainViewModel(fileExplorer: FileExplorer) : BasicViewModel(), FileExplorer by fileExplorer {

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer = LocalFileExplorer(initialPath, LocalFileLister())

    private val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> get() = _currentList


    override fun getFileLister(): FileLister = fileExplorer

    override fun fileExplorer(): FileExplorer = fileExplorer

    override fun listCurrentPath(): List<FSItem> {
        val list = fileExplorer.listCurrentPath()
        _currentList.value = fileExplorer.listCurrentPath()
        return list
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val fileExplorer: FileExplorer = LocalFileExplorer(
                    Environment.getExternalStorageDirectory().path, LocalFileLister()
                )

                return MainViewModel(fileExplorer) as T
            }
        }
    }
}