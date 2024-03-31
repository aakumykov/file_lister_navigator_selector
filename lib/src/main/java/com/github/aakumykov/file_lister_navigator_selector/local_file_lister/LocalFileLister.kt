package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String
)
    : FileLister
{
    override fun listDir(
        path: String,
        fsItemSortingComparator: FSItemSortingComparator
    )
        : List<FSItem>
    {
        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                val absolutePath = path + FSItem.DS + name
                val file = File(absolutePath)
                fileList.add(SimpleFSItem(file))
            }
        }

        return categorizeFSItems(fileList.toList()).sortedWith(fsItemSortingComparator)
    }
}