package com.github.aakumykov.local_file_explorer

import com.github.aakumykov.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_lister.LocalFileLister

class LocalFileExplorer(
    initialPath: String,
    localFileLister: LocalFileLister,
    listCache: FileLister.ListCache,
    pathCache: FileExplorer.PathCache
)
    : BasicFileExplorer(initialPath, listCache, pathCache), FileLister by localFileLister