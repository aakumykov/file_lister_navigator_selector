package com.github.aakumykov.file_lister_navigator_selector.file_explorer

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

/**
 * Предназначение - осуществлять навигацию по файловой системе.
 * Для этого реализации класса (ожидаемо) хранят состояние: начальный каталог (считающийся корневым)
 * и текущий путь.
 */
interface FileExplorer<SortingModeType> {

    // TODO: оставить только changeDir
    fun changeDir(dirItem: DirItem) // TODO: выброс исключений...

    fun listCurrentPath(): List<FSItem> // TODO: throws NotADirException

    fun getCurrentPath(): String
    fun getCurrentDir(): DirItem

    fun setSortingMode(sortingMode: SortingModeType)

    fun setPathCache(pathCache: PathCache)
    fun setListCache(listCache: ListCache)


    interface PathCache {
        fun cachePath(path: String)
    }

    interface ListCache {
        fun cacheList(list: List<FSItem>)
    }
}