package com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister

class LocalFileExplorer(
    initialPath: String,
    isDirMode: Boolean = false,
    localFileLister: LocalFileLister,
    listCache: com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer.ListCache?,
    pathCache: com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer.PathCache?
)
    : com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer(
        initialPath = initialPath,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    ), com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister by localFileLister