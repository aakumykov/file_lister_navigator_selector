package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister

class YandexDiskFileExplorer (
    yandexDiskFileLister: YandexDiskFileLister,
    initialPath: String,
    isDirMode: Boolean = false,
    defaultSortingMode: SortingMode = SortingMode.NAME_DIRECT,
    listCache: FileExplorer.ListCache? = null,
    pathCache: FileExplorer.PathCache? = null
)
    : BasicFileExplorer<SortingMode>(
        fileLister = yandexDiskFileLister,
        initialPath = initialPath,
        isDirMode = isDirMode,
        initialSortingMode = defaultSortingMode,
        listCache = listCache,
        pathCache = pathCache
    )