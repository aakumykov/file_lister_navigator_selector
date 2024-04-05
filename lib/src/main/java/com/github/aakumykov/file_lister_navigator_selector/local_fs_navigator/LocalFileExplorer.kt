package com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator

class LocalFileExplorer (
    localFileLister: FileLister<SortingMode>,
    localDirCreator: LocalDirCreator,
    initialPath: String,
    defaultSortingMode: SortingMode = SortingMode.NAME_DIRECT,
    isDirMode: Boolean = false,
    listCache: FileExplorer.ListCache?,
    pathCache: FileExplorer.PathCache?
)
    : BasicFileExplorer<SortingMode>(
        fileLister = localFileLister,
        dirCreator = localDirCreator,
        initialPath = initialPath,
        defaultSortingMode = defaultSortingMode,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache
    )