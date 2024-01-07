package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String
)
    : com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
{
    override fun listDir(path: String): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> {

        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val absolutePath = path + com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem.DS + name
                val file = File(absolutePath)
                val simpleFSItem = SimpleFSItem(file)
                fileList.add(simpleFSItem)
            }
        }

        return fileList
    }
}