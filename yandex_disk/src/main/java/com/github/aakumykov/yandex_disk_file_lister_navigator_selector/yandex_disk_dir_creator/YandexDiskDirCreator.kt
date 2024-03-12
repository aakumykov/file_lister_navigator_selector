package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.yandex_disk_client.YandexDiskClient

class YandexDiskDirCreator(private val yandexDiskClient: YandexDiskClient<FSItem, FileSortingMode>) : DirCreator {

    override fun makeDir(path: String): Boolean {
        yandexDiskClient.createDir(path)
        return true
    }
}