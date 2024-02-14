package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator

import com.github.aakumykov.cloud_writer.CloudWriter
import java.io.IOException

abstract class CloudDirCreator {

    abstract fun cloudWriter(): CloudWriter

    @Throws(
        IOException::class,
        CloudWriter.UnsuccessfulResponseException::class,
        CloudWriter.AlreadyExistsException::class
    )
    fun createDir(dirName: String, basePath: String) {
        cloudWriter().createDir(basePath, dirName)
    }
}