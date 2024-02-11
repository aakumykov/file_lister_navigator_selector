package com.github.aakumykov.kotlin_playground.cloud_dir_creator

import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator.Companion.DS
import com.github.aakumykov.kotlin_playground.extensions.stripExtraSlashes
import java.io.File

class LocalDirCreator : CloudDirCreator {

    @Throws(Exception::class)
    override suspend fun createDir(dirName: String, basePath: String) {
        val path = (basePath + DS + dirName).stripExtraSlashes()
        File(path).mkdir() || throw Exception("Dir not created.")
    }
}
