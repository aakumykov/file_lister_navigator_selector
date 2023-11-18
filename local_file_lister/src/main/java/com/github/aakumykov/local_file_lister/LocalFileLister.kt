package com.github.aakumykov.local_file_lister

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.ParentDirItem
import com.github.aakumykov.file_lister.SimpleFSItem
import java.io.File

class LocalFileLister(private val dirSeparator: String = FileLister.DS) : FileLister {

    override fun listDir(path: String): List<FSItem> {

        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val filePath = path + dirSeparator + name
                val file = File(filePath)
                fileList.add(SimpleFSItem(name, filePath, file.isDirectory))
            }
        }

        return fileList
    }
}