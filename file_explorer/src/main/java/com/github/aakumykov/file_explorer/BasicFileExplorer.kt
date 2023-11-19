package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.ParentDirItem

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private var listCache: FileExplorer.ListCache?,
    private var pathCache: FileExplorer.PathCache?,
    private val dirSeparator: String = FileLister.DS
) : FileExplorer {

    private var currentPath: String = initialPath

    override fun getCurrentPath(): String = currentPath

    override fun listCurrentPath(): List<FSItem> {
        val list = listOf(ParentDirItem()) + listDir(getCurrentPath())
        listCache?.cacheList(list)
        return list
    }

    override fun changeDir(fsItem: FSItem) {
        when (fsItem) {
            is ParentDirItem -> goToParentDir()
            else -> goToChildDir(fsItem.absolutePath)
        }
    }

    override fun goToRootDir() {
        changeDir(FileExplorer.ROOT_DIR_PATH)
    }

    override fun goToParentDir() {
        val pathParts = currentPath.split(dirSeparator)
        val parentPathParts = pathParts.subList(0, pathParts.size-1)
        var parentPath = parentPathParts.joinToString(separator = dirSeparator)
        if (parentPath.isEmpty())
            parentPath = initialPath
        changeDir(parentPath)
    }

    override fun goToChildDir(dirPath: String) {
        changeDir(dirPath)
    }

    override fun setPathCache(pathCache: FileExplorer.PathCache) {
        this.pathCache = pathCache
    }

    override fun setListCache(listCache: FileExplorer.ListCache) {
        this.listCache = listCache
    }


    private fun changeDir(path: String) {
        currentPath = path
        pathCache?.cachePath(currentPath)
    }
}