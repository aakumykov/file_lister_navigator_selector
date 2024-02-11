package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator

import com.github.aakumykov.cloud_writer.CloudWriter
import com.github.aakumykov.cloud_writer.YandexCloudWriter
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator
import com.google.gson.Gson
import okhttp3.OkHttpClient

class YandexDirCreator(private val authToken: String) : CloudDirCreator {

    override suspend fun createDir(dirName: String, basePath: String) {
        cloudWriter().createDir(basePath, dirName)
    }

    private fun cloudWriter(): CloudWriter {
        return YandexCloudWriter(OkHttpClient(), Gson(), authToken)
    }
}