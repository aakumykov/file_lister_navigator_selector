package com.github.aakumykov.local_file_explorer

import com.github.aakumykov.file_explorer.BasicFileExplorer
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.local_file_lister.LocalFileLister

class LocalFileExplorer(
    initialPath: String,
    localFileLister: LocalFileLister
) :
    BasicFileExplorer(initialPath),
    FileLister by localFileLister
{

}