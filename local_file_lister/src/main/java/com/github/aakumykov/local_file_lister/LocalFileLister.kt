package com.github.aakumykov.local_file_lister

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.SimpleFSItem

class LocalFileLister(private val initialPath: String) : FileLister {

    override fun listRootDir(): List<FSItem>
        = listDir(initialPath)

    override fun listDir(path: String): List<FSItem> {
        return listOf(
            SimpleFSItem("1","/1", true),
            SimpleFSItem("2","/2", true),
            SimpleFSItem("3","/3", true)
        )
    }
}