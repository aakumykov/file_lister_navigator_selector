package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator.YandexDiskDirCreator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister

class YandexDiskFileExplorer (
    yandexDiskFileLister: YandexDiskFileLister,
    yandexDiskDirCreator: YandexDiskDirCreator,
    defaultSortingMode: SortingMode = SortingMode.NAME_DIRECT,
    isDirMode: Boolean = false,
    initialPath: String = FileExplorer.ROOT_DIR_PATH,
    listCache: FileExplorer.ListCache? = null, // FIXME: привести аргументы всех потомков к единообразию
    pathCache: FileExplorer.PathCache? = null
)
    : BasicFileExplorer<SortingMode> (
        initialPath = initialPath,
        fileLister = yandexDiskFileLister,
        dirCreator = yandexDiskDirCreator,
        defaultSortingMode = defaultSortingMode,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    )