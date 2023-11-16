package com.github.aakumykov.kotlin_playground

import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FileLister

abstract class BasicViewModel : ViewModel() {

    abstract fun getFileExplorer(): FileExplorer
}