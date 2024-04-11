package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(
    @Assisted private val authToken: String
)
    : FileLister<SimpleSortingMode>
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    /**
     * Параметр foldersFirst не имеет эффекта.
     */
    override fun listDir(
        path: String,
        sortingMode: SimpleSortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean
    )
        : List<FSItem>
    {
        return yandexDiskClient
            .listDir(path, sortingMode, reverseOrder)
            .map { convertDirToDirItem(it) }
    }
}
