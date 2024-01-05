package com.github.aakumykov.file_lister_navigator_selector.yandex_disk_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister

class YandexDiskFileExplorer(
    yandexDiskFileLister: YandexDiskFileLister,
    isDirMode: Boolean = false,
    initialPath: String = com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer.ROOT_DIR_PATH,
    listCache: com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer.ListCache? = null, // FIXME: привести аргументы всех потомков к единообразию
    pathCache: com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer.PathCache? = null
)
    : com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer(
        initialPath = initialPath,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    ), com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister by yandexDiskFileLister