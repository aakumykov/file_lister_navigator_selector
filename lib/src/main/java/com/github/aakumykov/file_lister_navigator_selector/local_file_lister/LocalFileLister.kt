package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String
)
    : FileLister<SortingMode>
{
    override fun listDir(
        path: String,
        sortingMode: SortingMode
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

//        return categorizeFSItems(fileList.toList())
//            .sortedWith(sortingComparator(sortingMode))

        return fileList.toList()
//            .let { categorizeFSItems(it) }
            .sortedWith(FSItemSortingComparator.create(sortingMode))
    }
}