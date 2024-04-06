package com.github.aakumykov.file_lister_navigator_selector.file_explorer

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem

// FIXME: перенести кеш в реализацию
// TODO: добавить параметр типа "SortingModeType" в FileExplorer.
abstract class BasicFileExplorer<SortingModeType> (
    private val fileLister: FileLister<SortingModeType>,
    private val initialPath: String,
    private val isDirMode: Boolean,
    private val initialSortingMode: SortingModeType,
    private var listCache: FileExplorer.ListCache?, // TODO: сделать val
    private var pathCache: FileExplorer.PathCache?, // TODO: сделать val
    private val dirSeparator: String = FSItem.DS
)
    : FileExplorer<SortingModeType>
{
    private var currentPath: String = initialPath
    private var currentDir: DirItem = DirItem.fromPath(initialPath)

    private var currentSortingMode: SortingModeType = initialSortingMode

    private val currentList: MutableList<FSItem> = mutableListOf()

    override fun getCurrentPath(): String = currentPath
    override fun getCurrentDir(): DirItem = currentDir

    override fun setSortingMode(sortingMode: SortingModeType) {
        currentSortingMode = sortingMode
    }

    override fun listCurrentPath(): List<FSItem> {

        val rawDirList = fileLister.listDir(currentPath, currentSortingMode)

        currentList.clear()
        currentList.add(ParentDirItem())

        if (isDirMode) {
            val filteredList = rawDirList.filter { fsItem -> fsItem.isDir }
            currentList.addAll(filteredList)
        } else {
            currentList.addAll(rawDirList)
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