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

    // TODO: обрабатывать через на массив
    override fun goToParentDir() {
        currentPath = currentPath.replace(Regex("/[^/]+/?$"), "")
        if (currentPath.isEmpty())
            currentPath = FileExplorer.ROOT_DIR_PATH

        /*val pathParts = currentPath.split(dirSeparator)
        val parentPathParts = pathParts.subList(0, pathParts.size-2)
        currentPath = parentPathParts.joinToString(separator = dirSeparator)*/
    }

    override fun goToChildDir(dirName: String) {
        currentPath += dirSeparator + dirName
        currentPath = currentPath.replace(Regex("($dirSeparator)+"), dirSeparator)
    }
}