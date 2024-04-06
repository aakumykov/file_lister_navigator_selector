package com.github.aakumykov.file_lister_navigator_selector.local_fs_navigator

import com.github.aakumykov.file_lister_navigator_selector.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.local_dir_creator.LocalDirCreator
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister

class LocalFileExplorer(
    localFileLister: LocalFileLister,
    localDirCreator: LocalDirCreator,
    initialPath: String,
    isDirMode: Boolean = false,
    defaultSortingMode: SortingMode = SortingMode.NAME_DIRECT,
    listCache: FileExplorer.ListCache? = null,
    pathCache: FileExplorer.PathCache? = null
)
: BasicFileExplorer<SortingMode> (
    fileLister = localFileLister,
    dirCreator = localDirCreator,
    initialPath = initialPath,
    isDirMode = isDirMode,
    initialSortingMode = defaultSortingMode,
    listCache = listCache,
    pathCache = pathCache
)