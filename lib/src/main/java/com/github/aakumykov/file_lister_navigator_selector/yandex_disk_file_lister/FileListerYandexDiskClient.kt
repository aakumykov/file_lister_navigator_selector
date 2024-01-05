package com.github.aakumykov.file_lister_navigator_selector.yandex_disk_file_lister

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

    override fun cloudItemToLocalItem(resource: Resource): com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem {
        val name = resource.name
        val path = resource.path.path
        val isDir = resource.isDir
        val cTime = resource.created.time
        return com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem(
            name,
            path,
            isDir,
            cTime
        )
    }

    override fun cloudFileToString(resource: Resource): String {
        return "Yandex Disk item '${resource.name}'"
    }
}
