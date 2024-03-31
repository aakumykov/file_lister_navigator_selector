package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.BasicFileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.DummySortingComparator
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(@Assisted private val authToken: String)
    : BasicFileLister()
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun sortingComparator(externalSortingMode: SortingMode): FSItemSortingComparator {
        return DummySortingComparator()
    }

    override fun listDir(
        path: String,
        sortingMode: SortingMode
    ): List<FSItem> {
        return categorizeFSItems(
            yandexDiskClient.listDir(path, sortingMode)
        )
    }
}
