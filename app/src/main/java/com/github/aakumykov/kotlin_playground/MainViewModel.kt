package com.github.aakumykov.kotlin_playground

import android.os.Environment
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_explorer.LocalFileExplorer
import com.github.aakumykov.local_file_lister.LocalFileLister

class MainViewModel : BasicViewModel() {

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path

    private var _initialPath: String? = null
    val initialPath get() = _initialPath ?: DEFAULT_INITIAL_PATH

    private val fileExplorer: FileExplorer = LocalFileExplorer(initialPath, LocalFileLister())


    override fun getFileLister(): FileLister {
        return fileExplorer
    }

    override fun getFileExplorer(): FileExplorer {
        return fileExplorer
    }
}