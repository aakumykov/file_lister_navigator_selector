package com.github.aakumykov.file_lister_navigator_selector.file_lister

import java.io.IOException

/**
 * Задача реализаций этого интерфейса просто выдавать список файлов по указанному пути.
 * Они не должны хранить состояние.
 */
interface FileLister {

    /**
     * Возвращает список файлов (объектов с интерфейсом FSItem) по указанному пути.
     * Если расположение, указанное в аргументе, не удаётся прочитать, возвращает пустой список.
     * Может выбрасывать исключения, если реализации предполагают их.
     */
    @Throws(NotADirException::class)
    fun listDir(path: String): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>

    class NotADirException : IOException()
}
