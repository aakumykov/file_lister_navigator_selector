package com.github.aakumykov.file_lister_navigator_selector.fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private val isDirMode: Boolean,
    private val initialSortingMode: FileSortingMode = FileSortingMode.NAME_DIRECT,
    private var listCache: FileExplorer.ListCache?, // TODO: сделать val
    private var pathCache: FileExplorer.PathCache?, // TODO: сделать val
    private val dirSeparator: String = FSItem.DS
) : FileExplorer, DirCreator {

    private var currentPath: String = initialPath
    private var currentDir: DirItem = DirItem.fromPath(initialPath)

    private var currentSortingMode: FileSortingMode = initialSortingMode
    private var currentOffset: Int = 0
    private var currentLimit: Int = -1

    private val currentList: MutableList<FSItem> = mutableListOf()

    override fun getCurrentPath(): String = currentPath

    override fun getCurrentDir(): DirItem = currentDir


    override fun listCurrentPath(): List<FSItem> {
        return listCurrentPath(currentSortingMode, currentOffset, currentLimit)
    }

    override fun listCurrentPath(
        sortingMode: FileSortingMode,
        offset: Int,
        limit: Int
    ): List<FSItem> {

        currentSortingMode = sortingMode
        currentOffset = offset
        currentLimit = limit

        val initialList = listDir(getCurrentPath(), currentSortingMode, currentOffset, currentLimit)

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

    override fun changeDir(dirItem: DirItem) {
        when (dirItem) {
            is ParentDirItem -> goToParentDir()
            else -> goToChildDir(dirItem.absolutePath)
        }
    }

    override fun goToRootDir() {
        changeCurrentPath(FileExplorer.ROOT_DIR_PATH)
    }

    override fun goToParentDir() {
        val pathParts = currentPath.split(dirSeparator)
        val parentPathParts = pathParts.subList(0, pathParts.size-1)
        var parentPath = parentPathParts.joinToString(separator = dirSeparator)
        if (parentPath.isEmpty())
            parentPath = initialPath

        changeCurrentPath(parentPath)
    }

    override fun goToChildDir(dirPath: String) {
        changeCurrentPath(dirPath)
    }

    override fun setPathCache(pathCache: FileExplorer.PathCache) {
        this.pathCache = pathCache
    }

    override fun setListCache(listCache: FileExplorer.ListCache) {
        this.listCache = listCache
    }


    private fun changeCurrentPath(path: String) {
        currentPath = path
        pathCache?.cachePath(currentPath)

        currentDir = DirItem.fromPath(path)
    }
}