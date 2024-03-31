package com.github.aakumykov.file_lister_navigator_selector.file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import java.io.IOException

/**
 * Задача реализаций этого интерфейса просто выдавать список файлов по указанному пути.
 * Они не должны хранить состояние.
 */
interface FileLister<SortingModeType> {

    /**
     * Возвращает список файлов (объектов с интерфейсом FSItem) по указанному пути.
     * Если расположение, указанное в аргументе, не удаётся прочитать, возвращает пустой список.
     * Может выбрасывать исключения, если реализации предполагают их.
     */
    @Throws(NotADirException::class)
    fun listDir(path: String, sortingMode: SortingModeType): List<FSItem>

    fun sortingComparator(externalSortingMode: SortingModeType): FSItemSortingComparator

    class NotADirException : IOException()
}
