package com.github.aakumykov.file_lister_navigator_selector.fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

/**
 * Предназначение - осуществлять навигацию по файловой системе.
 * Для этого реализации класса (ожидаемо) хранят состояние: начальный каталог (считающийся корневым)
 * и текущий путь.
 *
 * Интерфефс расширяет интерфейс FileLister, так как навигация в отрыве от получения списка файлов бессмысленна.
 */
interface FileExplorer<SortingModeType> {

    fun changeDir(dirItem: DirItem) // TODO: выброс исключений...

    // TODO: убрать, оставить только changeDir()
    fun goToChildDir(dirPath: String) // TODO: throws IOException, AccessDeniesException
    fun goToParentDir() // TODO: throws IOException, AccessDeniesException

    @Deprecated("Не используется?")
    fun goToRootDir() // TODO: throws IOException, AccessDeniesException

    fun listCurrentPath(): List<FSItem> // TODO: throws NotADirException

    @Deprecated("Не используется?")
    fun getCurrentPath(): String

    @Deprecated("Не используется?")
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


    companion object {
        // Используется в качестве "последнего пути", когда переходы в родительский
        // каталог приводят к тому, что секущий путь превращается в пустую строку.
        @Deprecated("Избавиться, так как в реализациях корневой каталог разный")
        const val ROOT_DIR_PATH: String = "/"
    }
}