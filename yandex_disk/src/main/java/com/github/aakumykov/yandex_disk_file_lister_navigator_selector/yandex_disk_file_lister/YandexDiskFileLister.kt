package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
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

    @Deprecated("Используй метод с параметром сортировки")
    override fun listDir(path: String): List<FSItem> {
        return yandexDiskClient.listDir(path)
    }

    override fun listDir(path: String, sortingMode: FileSortingMode, offset: Int, limit: Int): List<FSItem> {
        return categorizeFSItems(
            yandexDiskClient.listDir(path, sortingMode, offset, limit)
        )
    }
}
