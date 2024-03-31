package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(@Assisted private val authToken: String) : FileLister
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun listDir(
        path: String,
        fsItemSortingComparator: FSItemSortingComparator
    ): List<FSItem> {
        return categorizeFSItems(
            yandexDiskClient.listDir(path, fsItemSortingComparator.sortingMode)
        )
    }
}
