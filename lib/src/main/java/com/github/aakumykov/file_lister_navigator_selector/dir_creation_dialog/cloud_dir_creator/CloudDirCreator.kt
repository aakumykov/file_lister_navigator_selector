package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator

interface CloudDirCreator {

    @Throws(Exception::class, AlreadyExistsException::class)
    suspend fun createDir(dirName: String, basePath: String)

    class AlreadyExistsException : Exception()
}