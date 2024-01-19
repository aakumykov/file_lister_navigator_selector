package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(
    @Assisted private val authToken: String
)
    : com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun listDir(path: String): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> {
        return yandexDiskClient.listDir(path)
    }
}
