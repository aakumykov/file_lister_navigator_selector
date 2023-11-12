package com.github.aakumykov.kotlin_playground

import android.os.Environment
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_lister.LocalFileLister

class MainViewModel : BasicViewModel() {

    private val DEFAULT_INITIAL_PATH = Environment.getExternalStorageDirectory().path
    private val localFileLister: FileLister

    init {
        localFileLister = LocalFileLister(DEFAULT_INITIAL_PATH)
    }

    override fun fileLister(): FileLister {
        return localFileLister
    }
}