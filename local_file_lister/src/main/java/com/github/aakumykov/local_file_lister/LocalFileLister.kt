package com.github.aakumykov.local_file_lister

import android.webkit.PluginStub
import com.github.aakumykov.fs_item.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.fs_item.SimpleFSItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String
)
    : FileLister
{
    override fun listDir(path: String): List<FSItem> {

        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val absolutePath = path + FSItem.DS + name
                val file = File(absolutePath)
                val simpleFSItem = SimpleFSItem(file)
                fileList.add(simpleFSItem)
            }
        }

        return fileList
    }
}