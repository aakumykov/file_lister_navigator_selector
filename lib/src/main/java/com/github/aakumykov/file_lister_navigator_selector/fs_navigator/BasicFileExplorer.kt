package com.github.aakumykov.file_lister_navigator_selector.fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingComparator
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private val isDirMode: Boolean,
    private var listCache: FileExplorer.ListCache?,
    private var pathCache: FileExplorer.PathCache?,
    private val dirSeparator: String = FSItem.DS
) : FileExplorer {

    private var currentPath: String = initialPath
    private val currentList: MutableList<FSItem> = mutableListOf()

    override fun getCurrentPath(): String = currentPath

    override fun listCurrentPath(): List<FSItem> {
        return listCurrentPathReal(null)
    }

    override fun listCurrentPath(sortingComparator: SortingComparator): List<FSItem> {
        return listCurrentPathReal(sortingComparator)
    }

    // TODO: применить конвейер?
    private fun listCurrentPathReal(sortingComparator: SortingComparator? = null): List<FSItem> {

        val initialList = listDir(getCurrentPath())

        val filteredList: List<FSItem> =
            if (isDirMode) initialList.filter { fsItem -> fsItem.isDir }
            else initialList

        val sortedList = sortingComparator?.let { filteredList.sortedWith(sortingComparator) } ?: filteredList

        currentList.apply {
            clear()
            add(ParentDirItem())
            addAll(sortedList)
        }.also {
            listCache?.cacheList(it)
        }

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