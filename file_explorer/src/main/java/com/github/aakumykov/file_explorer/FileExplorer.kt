package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FileLister

interface FileExplorer : FileLister {

    fun goToChildDir(dirName: String)
    fun goToParentDir()
    fun goToRootDir()

    fun getCurrentPath(): String

    companion object {
        const val DS = "/"
    }
}