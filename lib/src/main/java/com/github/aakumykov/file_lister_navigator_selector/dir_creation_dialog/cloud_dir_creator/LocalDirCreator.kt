package com.github.aakumykov.kotlin_playground.cloud_dir_creator

import com.github.aakumykov.cloud_writer.CloudWriter
import com.github.aakumykov.cloud_writer.LocalCloudWriter
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.CloudDirCreator

class LocalDirCreator : CloudDirCreator() {

    override fun cloudWriter(): CloudWriter = LocalCloudWriter(AUTH_TOKEN_STUB)

    companion object {
        const val AUTH_TOKEN_STUB = ""
    }
}
