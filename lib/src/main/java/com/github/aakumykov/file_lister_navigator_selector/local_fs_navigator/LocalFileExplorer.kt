package com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister

class LocalFileExplorer<T>(
    initialPath: String,
    isDirMode: Boolean = false,
    localFileLister: LocalFileLister<T>,
    localDirCreator: LocalDirCreator,
    defaultSortingMode: T,
    listCache: FileExplorer.ListCache?,
    pathCache: FileExplorer.PathCache?
)
    : BasicFileExplorer<T>(
        initialPath = initialPath,
        isDirMode = isDirMode,
        listCache = listCache,
        pathCache = pathCache,
        defaultSortingMode = defaultSortingMode
    ),

    FileLister<T> by localFileLister,

    DirCreator by localDirCreator