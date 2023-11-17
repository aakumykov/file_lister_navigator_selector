package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private val listCache: FileLister.ListCache? = null,
    private val pathCache: FileExplorer.PathCache? = null,
    private val dirSeparator: String = FileLister.DS
) : FileExplorer {

    private var currentPath: String = initialPath

    override fun getCurrentPath(): String = currentPath

    override fun listCurrentPath(): List<FSItem> {
        val list = listDir(getCurrentPath())
        listCache?.cacheList(list)
        return list
    }

    override fun goToRootDir() {
        changeDir(FileExplorer.ROOT_DIR_PATH)
    }

    // TODO: обрабатывать через на массив
    override fun goToParentDir() {
        val pathParts = currentPath.split(dirSeparator)
        val parentPathParts = pathParts.subList(0, pathParts.size-1)
        val parentPath = parentPathParts.joinToString(separator = dirSeparator)
        changeDir(parentPath)
    }

    override fun goToChildDir(dirName: String) {
        var childPath = currentPath + dirSeparator + dirName
        childPath = childPath.replace(Regex("($dirSeparator)+"), dirSeparator)
        changeDir(childPath)
    }

    private fun changeDir(path: String) {
        currentPath = path
        pathCache?.cachePath(currentPath)
    }
}