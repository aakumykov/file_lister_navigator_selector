package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.ParentDirItem

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private val isDirMode: Boolean = false,
    private var listCache: FileExplorer.ListCache?,
    private var pathCache: FileExplorer.PathCache?,
    private val dirSeparator: String = FileLister.DS
) : FileExplorer {

    private var currentPath: String = initialPath
    private val currentList: MutableList<FSItem> = mutableListOf()

    override fun getCurrentPath(): String = currentPath

    override fun listCurrentPath(): List<FSItem> {

        val initialList = listDir(getCurrentPath())

        currentList.clear()
        currentList.add(ParentDirItem())

        if (isDirMode) {
            val filteredList = initialList.filter { fsItem -> fsItem.isDir }
            currentList.addAll(filteredList)
        } else {
            currentList.addAll(initialList)
        }

        listCache?.cacheList(currentList)

        return currentList
    }

    override fun changeDir(fsItem: FSItem) {
        when (fsItem) {
            is ParentDirItem -> goToParentDir()
            else -> goToChildDir(fsItem.absolutePath)
        }
    }

    override fun goToRootDir() {
        changePath(FileExplorer.ROOT_DIR_PATH)
    }

    override fun goToParentDir() {
        val pathParts = currentPath.split(dirSeparator)
        val parentPathParts = pathParts.subList(0, pathParts.size-1)
        var parentPath = parentPathParts.joinToString(separator = dirSeparator)
        if (parentPath.isEmpty())
            parentPath = initialPath
        changePath(parentPath)
    }

    override fun goToChildDir(dirPath: String) {
        changePath(dirPath)
    }

    override fun setPathCache(pathCache: FileExplorer.PathCache) {
        this.pathCache = pathCache
    }

    override fun setListCache(listCache: FileExplorer.ListCache) {
        this.listCache = listCache
    }


    private fun changePath(path: String) {
        currentPath = path
        pathCache?.cachePath(currentPath)
    }
}