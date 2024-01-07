package com.github.aakumykov.file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import com.github.aakumykov.yandex_disk_client.YandexDiskClient
import com.github.aakumykov.yandex_disk_client.YandexDiskSortingMode
import com.yandex.disk.rest.json.Resource

internal class FileListerYandexDiskClient(authToken: String)
    : YandexDiskClient<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem, com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode>(authToken)
{
    override fun appToDiskSortingMode(appSortingMode: com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode): YandexDiskSortingMode {
        return when(appSortingMode) {
            com.github.aakumykov.file_lister_navigator_selector.file_lister.FileSortingMode.NAME_REVERSE -> YandexDiskSortingMode.NAME_REVERSE
            else -> YandexDiskSortingMode.NAME_DIRECT
        }
    }

    override fun cloudItemToLocalItem(resource: Resource): FSItem {

        val path = resource.path.path
        val parentPath = parentPathFor(path)

        return SimpleFSItem(
            resource.name,
            path,
            parentPath,
            resource.isDir,
            resource.created.time
        )
    }

    override fun cloudFileToString(resource: Resource): String {
        return "Yandex Disk item '${resource.name}'"
    }
}
