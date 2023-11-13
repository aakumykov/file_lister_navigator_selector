package com.github.aakumykov.local_file_lister

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.SimpleFSItem
import java.io.File

class LocalFileLister : FileLister {

    // TODO: проверить с выбросом исключения
    override fun listDir(path: String): List<FSItem> {

        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val filePath = path + FileLister.DS + name
                val file = File(filePath)
                fileList.add(SimpleFSItem(name, filePath, file.isDirectory))
            }
        }

        return fileList
    }
}