package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FileItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import com.github.aakumykov.yandex_disk_client.YandexDiskClient
import com.github.aakumykov.yandex_disk_client.YandexDiskSortingMode
import com.yandex.disk.rest.json.Resource

class FileListerYandexDiskClient(authToken: String)
    : YandexDiskClient<FSItem, FileSortingMode>(authToken)
{
    override fun appToDiskSortingMode(appSortingMode: FileSortingMode): YandexDiskSortingMode {
        return when(appSortingMode) {
            FileSortingMode.NAME_DIRECT -> YandexDiskSortingMode.NAME_DIRECT
            FileSortingMode.NAME_REVERSE -> YandexDiskSortingMode.NAME_REVERSE

            FileSortingMode.C_TIME_DIRECT -> YandexDiskSortingMode.C_TIME_FROM_OLD_TO_NEW
            FileSortingMode.C_TIME_REVERSE -> YandexDiskSortingMode.C_TIME_FROM_NEW_TO_OLD

            FileSortingMode.M_TIME_DIRECT -> YandexDiskSortingMode.M_TIME_FROM_OLD_TO_NEW
            FileSortingMode.M_TIME_REVERSE -> YandexDiskSortingMode.M_TIME_FROM_NEW_TO_OLD

            FileSortingMode.SIZE_DIRECT -> YandexDiskSortingMode.SIZE_FROM_SMALL_TO_BIG
            FileSortingMode.SIZE_REVERSE -> YandexDiskSortingMode.SIZE_FROM_BIG_TO_SMALL
        }
    }

    override fun cloudItemToLocalItem(resource: Resource): FSItem {

        val path = resource.path.path
        val parentPath = parentPathFor(path)

        return FileItem(
            name = resource.name,
            absolutePath = path,
            parentPath = parentPath,
            isDir = resource.isDir,
            mTime = resource.modified.time,
            size = resource.size
        )
    }

    override fun cloudFileToString(resource: Resource): String {
        return "Yandex Disk item '${resource.name}'"
    }
}
