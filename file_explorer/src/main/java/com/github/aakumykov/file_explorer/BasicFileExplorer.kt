package com.github.aakumykov.file_explorer

abstract class BasicFileExplorer(
    private val initialPath: String
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
        currentPath += FileExplorer.DS + dirName
        currentPath = currentPath.replace(Regex("(${FileExplorer.DS})+"), FileExplorer.DS)
    }
}