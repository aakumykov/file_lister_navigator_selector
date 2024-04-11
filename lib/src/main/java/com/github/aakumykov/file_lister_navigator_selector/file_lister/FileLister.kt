package com.github.aakumykov.file_lister_navigator_selector.file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import java.io.IOException

/**
 * Задача реализаций этого интерфейса просто выдавать список файлов по указанному пути.
 * Они не должны хранить состояние.
 */
interface FileLister<SortingModeType> {

    /**
     * Возвращает список файлов (объектов с интерфейсом FSItem) по указанному пути.
     * Если расположение, указанное в аргументе, не удаётся прочитать, возвращает пустой список.
     * Может выбрасывать исключения (в зависимости от реализации).
     *
     * Ответственность реализаций использовать SortingModeType по своему усмотрению:
     * например, LocalFileLister на его основе создаёт компаратор, который сортирует полученный список,
     * YandexDiskFileLister преобразует SortingModeType в строковый аргумент типа сортировки для запроса
     * к облаку.
     */
    @Throws(NotADirException::class)
    fun listDir(
        path: String,
        sortingMode: SortingModeType,
        reverseOrder: Boolean,
        foldersFirst: Boolean
    ): List<FSItem>


    @Deprecated("Избавиться, так как используется только в RecursiveDirReader")
    class NotADirException : IOException()
}
