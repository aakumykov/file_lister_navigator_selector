package com.github.aakumykov.kotlin_playground

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class MainViewModel : BasicViewModel(), FileExplorer {

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer = LocalFileExplorer(initialPath, LocalFileLister())

    private val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData()
    val currentList: LiveData<List<FSItem>> = _currentList


    override fun getFileLister(): FileLister = fileExplorer

    override fun fileExplorer(): FileExplorer = fileExplorer


    override fun goToChildDir(dirName: String) {
        TODO("Not yet implemented")
    }

    override fun goToParentDir() {
        TODO("Not yet implemented")
    }

    override fun goToRootDir() {
        TODO("Not yet implemented")
    }

    override fun listCurrentPath(): List<FSItem> = fileExplorer().listCurrentPath()

    override fun getCurrentPath(): String {
        TODO("Not yet implemented")
    }

    override fun listDir(path: String): List<FSItem> {
        val list = listCurrentPath()
        _currentList.value = list
        return list
    }
}