package com.github.aakumykov.file_lister_navigator_selector.fs_navigator

// FIXME: перенести кеш в реализацию
abstract class BasicFileExplorer(
    private val initialPath: String,
    private val isDirMode: Boolean,
    private var listCache: FileExplorer.ListCache?,
    private var pathCache: FileExplorer.PathCache?,
    private val dirSeparator: String = com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem.DS
) : FileExplorer {

    private var currentPath: String = initialPath
    private val currentList: MutableList<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> = mutableListOf()

    override fun getCurrentPath(): String = currentPath

    override fun listCurrentPath(): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> {

        val initialList = listDir(getCurrentPath())

        currentList.clear()
        currentList.add(com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem())

        if (isDirMode) {
            val filteredList = initialList.filter { fsItem -> fsItem.isDir }
            currentList.addAll(filteredList)
        } else {
            currentList.addAll(initialList)
        }

        listCache?.cacheList(currentList)

        return currentList
    }

    override fun changeDir(fsItem: com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem) {
        when (fsItem) {
            is com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem -> goToParentDir()
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