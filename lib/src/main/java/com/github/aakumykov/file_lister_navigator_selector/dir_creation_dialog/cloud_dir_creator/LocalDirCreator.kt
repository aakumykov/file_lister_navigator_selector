package com.github.aakumykov.kotlin_playground.cloud_dir_creator

import com.github.aakumykov.cloud_writer.stripExtraSlashes
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.CloudDirCreator
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem.Companion.DS
import java.io.File

class LocalDirCreator : CloudDirCreator {

    @Throws(
        Exception::class,
        CloudDirCreator.AlreadyExistsException::class
    )
    override suspend fun createDir(dirName: String, basePath: String) {

        val fullPath = (basePath + DS + dirName).stripExtraSlashes()

        val dir = File(fullPath)

        if (dir.exists()) throw CloudDirCreator.AlreadyExistsException()

        File(fullPath).mkdir() || throw Exception("Dir not created.")
    }
}
