package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.file_lister_navigator_selector.ComparatorFactory
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String,
    private val comparatorFactory: ComparatorFactory
)
    : FileLister<SortingMode>
{
    override fun listDir(
        path: String,
        sortingMode: SortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean
    )
        : List<FSItem>
    {
        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val absolutePath = path + FSItem.DS + name
                val file = File(absolutePath)
                fileList.add(if (file.isDirectory) DirItem(file) else SimpleFSItem(file))
            }
        }

        return fileList.toList()
            .sortedWith(comparatorFactory.createComparator(sortingMode, reverseOrder, foldersFirst))
    }
}