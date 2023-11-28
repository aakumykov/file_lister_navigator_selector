package com.github.aakumykov.yandex_disk_file_lister

import com.github.aakumykov.fs_item.FSItem
import com.github.aakumykov.file_lister.FileSortingMode
import com.github.aakumykov.fs_item.SimpleFSItem
import com.github.aakumykov.yandex_disk_client.YandexDiskSortingMode
import com.github.aakumykov.yandex_disk_client.YandexDiskClient
import com.yandex.disk.rest.json.Resource

internal class FileListerYandexDiskClient(authToken: String)
    : YandexDiskClient<FSItem, FileSortingMode>(authToken)
{
    override fun appToDiskSortingMode(appSortingMode: FileSortingMode): YandexDiskSortingMode {
        return when(appSortingMode) {
            FileSortingMode.NAME_REVERSE -> YandexDiskSortingMode.NAME_REVERSE
            else -> YandexDiskSortingMode.NAME_DIRECT
        }
    }

    override fun cloudItemToLocalItem(resource: Resource): FSItem {
        val name = resource.name
        val path = resource.path.path
        val isDir = resource.isDir

        return SimpleFSItem(name, path, isDir)
    }

    override fun cloudFileToString(resource: Resource): String {
        return "Yandex Disk item '${resource.name}'"
    }
}
