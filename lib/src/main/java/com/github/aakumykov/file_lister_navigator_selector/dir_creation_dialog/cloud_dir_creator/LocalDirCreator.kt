package com.github.aakumykov.kotlin_playground.cloud_dir_creator

import com.github.aakumykov.cloud_writer.stripExtraSlashes
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem.Companion.DS
import java.io.File

class LocalDirCreator : CloudDirCreator {

    @Throws(Exception::class)
    override suspend fun createDir(dirName: String, basePath: String) {
        val path = (basePath + DS + dirName).stripExtraSlashes()
        File(path).mkdir() || throw Exception("Dir not created.")
    }
}
