package com.github.aakumykov.yandex_disk_file_explorer

import com.github.aakumykov.yandex_disk_file_lister.YandexDiskFileLister
import com.github.aakumykov.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister

class YandexDiskFileExplorer(
    initialPath: String,
    yandexDiskFileLister: YandexDiskFileLister,
    listCache: FileLister.ListCache,
    pathCache: FileExplorer.PathCache
)
    : BasicFileExplorer(initialPath, listCache, pathCache), FileLister by yandexDiskFileLister