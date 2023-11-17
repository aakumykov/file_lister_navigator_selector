package com.github.aakumykov.file_lister

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
    fun listDir(path: String): List<FSItem> // TODO: throws NotADirException


    companion object {
        const val DS: String = "/"
        const val PARENT_DIR_NAME: String = ".."
        const val PARENT_DIR_PATH: String = ".."
    }
}
