package com.github.aakumykov.kotlin_playground.cloud_dir_creator

interface CloudDirCreator {

    @Throws(Exception::class)
    suspend fun createDir(dirName: String, basePath: String)

    companion object {
        const val DS = "/"
    }
}