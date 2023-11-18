package com.github.aakumykov.yandex_disk_file_lister

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.ParentDirItem

class YandexDiskFileLister(authToken: String, private val dirSeparator: String = FileLister.DS) : FileLister {

    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun listDir(path: String): List<FSItem> {
        return yandexDiskClient.listDir(path)
    }
}
