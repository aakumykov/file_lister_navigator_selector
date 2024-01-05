package com.github.aakumykov.file_lister_navigator_selector.fs_navigator

/**
 * Предназначение - осуществлять навигацию по файловой системе.
 * Для этого реализации класса (ожидаемо) хранят состояние: начальный каталог (считающийся корневым)
 * и текущий путь.
 *
 * Интерфефс расширяет интерфейс FileLister, так как навигация в отрыве от получения списка файлов бессмысленна.
 */
interface FileExplorer :
    com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister {

    fun changeDir(fsItem: com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem) // TODO: выброс исключений...
    fun goToChildDir(dirPath: String) // TODO: throws IOException, AccessDeniesException
    fun goToParentDir() // TODO: throws IOException, AccessDeniesException
    fun goToRootDir() // TODO: throws IOException, AccessDeniesException

    fun listCurrentPath(): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> // TODO: throws NotADirException
    fun getCurrentPath(): String

    fun setPathCache(pathCache: PathCache)
    fun setListCache(listCache: ListCache)

    interface PathCache {
        fun cachePath(path: String)
    }

    interface ListCache {
        fun cacheList(list: List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>)
    }


    companion object {
        // Используется в качестве "последнего пути", когда переходы в родительский
        // каталог приводят к тому, что секущий путь превращается в пустую строку.
        @Deprecated("Избавиться, так как в реализациях корневой каталог разный")
        const val ROOT_DIR_PATH: String = "/"
    }
}