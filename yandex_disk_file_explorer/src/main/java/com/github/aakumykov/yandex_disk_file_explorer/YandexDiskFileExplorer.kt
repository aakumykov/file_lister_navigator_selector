package com.github.aakumykov.yandex_disk_file_explorer

import com.github.aakumykov.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.yandex_disk_file_lister.YandexDiskFileLister

class YandexDiskFileExplorer(
    yandexDiskFileLister: YandexDiskFileLister,
    isDirMode: Boolean = false,
    initialPath: String = FileExplorer.ROOT_DIR_PATH,
    listCache: FileExplorer.ListCache? = null, // FIXME: привести аргументы всех потомков к единообразию
    pathCache: FileExplorer.PathCache? = null
)
    : BasicFileExplorer(
        initialPath = initialPath,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    ), FileLister by yandexDiskFileLister