package com.github.aakumykov.local_file_lister

import com.github.aakumykov.fs_item.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.fs_item.SimpleFSItem
import dagger.assisted.AssistedInject
import java.io.File
import javax.inject.Inject

class LocalFileLister @AssistedInject constructor(
    private val dirSeparator: String = FSItem.DS
)
    : FileLister
{
    override fun listDir(path: String): List<FSItem> {

        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val absolutePath = path + dirSeparator + name
                val file = File(absolutePath)
                val simpleFSItem = SimpleFSItem(file)
                fileList.add(simpleFSItem)
            }
        }

        return fileList
    }
}