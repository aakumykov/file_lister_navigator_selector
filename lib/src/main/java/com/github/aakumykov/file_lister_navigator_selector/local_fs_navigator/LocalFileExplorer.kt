package com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister

class LocalFileExplorer(
    initialPath: String,
    isDirMode: Boolean = false,
    localFileLister: LocalFileLister,
    listCache: FileExplorer.ListCache?,
    pathCache: FileExplorer.PathCache?
)
    : BasicFileExplorer(
        initialPath = initialPath,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    ), FileLister by localFileLister