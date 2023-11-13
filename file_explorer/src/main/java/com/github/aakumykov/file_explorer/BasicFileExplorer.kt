package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FileLister

abstract class BasicFileExplorer(
    private val initialPath: String,
    private val dirSeparator: String = FileLister.DS
) : FileExplorer {

    private var currentPath: String = initialPath

    override fun getCurrentPath(): String = currentPath

    fun setCurrentPath(path: String) {
        currentPath = path
    }

    override fun goToRootDir() {
        currentPath = initialPath
    }

    override fun goToParentDir() {
        currentPath = currentPath.replace(Regex("(..)/?$"), "")
    }

    override fun goToChildDir(dirName: String) {
        currentPath += dirSeparator + dirName
        currentPath = currentPath.replace(Regex("($dirSeparator)+"), dirSeparator)
    }
}