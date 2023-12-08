package com.github.aakumykov.yandex_disk_file_lister

import com.github.aakumykov.fs_item.FSItem
import com.github.aakumykov.file_lister.FileLister
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(
    @Assisted private val authToken: String
)
    : FileLister
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun listDir(path: String): List<FSItem> {
        return yandexDiskClient.listDir(path)
    }
}
