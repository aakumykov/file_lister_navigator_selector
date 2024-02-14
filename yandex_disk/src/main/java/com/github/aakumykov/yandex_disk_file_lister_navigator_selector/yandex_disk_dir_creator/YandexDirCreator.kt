package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator

import com.github.aakumykov.cloud_writer.CloudWriter
import com.github.aakumykov.cloud_writer.YandexCloudWriter
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.CloudDirCreator
import com.google.gson.Gson
import okhttp3.OkHttpClient

class YandexDirCreator(private val authToken: String) : CloudDirCreator() {

    override fun cloudWriter(): CloudWriter = YandexCloudWriter(OkHttpClient(), Gson(), authToken)
}