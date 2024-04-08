package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import com.github.aakumykov.yandex_disk_client.YandexDiskClient
import com.github.aakumykov.yandex_disk_client.YandexDiskSortingMode
import com.yandex.disk.rest.json.Resource

class FileListerYandexDiskClient(authToken: String)
    : YandexDiskClient<FSItem, SimpleSortingMode>(authToken)
{
    override fun appToDiskSortingMode(appSortingMode: SimpleSortingMode): YandexDiskSortingMode {
        return when(appSortingMode) {
            SimpleSortingMode.NAME_DIRECT -> YandexDiskSortingMode.NAME_DIRECT
            SimpleSortingMode.NAME_REVERSE -> YandexDiskSortingMode.NAME_REVERSE

            SimpleSortingMode.C_TIME_DIRECT -> YandexDiskSortingMode.C_TIME_FROM_OLD_TO_NEW
            SimpleSortingMode.C_TIME_REVERSE -> YandexDiskSortingMode.C_TIME_FROM_NEW_TO_OLD

            SimpleSortingMode.M_TIME_DIRECT -> YandexDiskSortingMode.M_TIME_FROM_OLD_TO_NEW
            SimpleSortingMode.M_TIME_REVERSE -> YandexDiskSortingMode.M_TIME_FROM_NEW_TO_OLD

            SimpleSortingMode.SIZE_DIRECT -> YandexDiskSortingMode.SIZE_FROM_SMALL_TO_BIG
            SimpleSortingMode.SIZE_REVERSE -> YandexDiskSortingMode.SIZE_FROM_BIG_TO_SMALL
        }
    }

    override fun cloudItemToLocalItem(resource: Resource): FSItem {

        val path = resource.path.path
        val parentPath = parentPathFor(path)

        return SimpleFSItem(
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
