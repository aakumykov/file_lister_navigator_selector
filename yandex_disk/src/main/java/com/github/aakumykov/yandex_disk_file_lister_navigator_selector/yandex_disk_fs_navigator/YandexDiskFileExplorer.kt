package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator.YandexDiskDirCreator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister

class YandexDiskFileExplorer(
    yandexDiskFileLister: YandexDiskFileLister,
    yandexDiskDirCreator: YandexDiskDirCreator,
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
    ),

    FileLister<SortingMode> by yandexDiskFileLister,

    DirCreator by yandexDiskDirCreator