package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String
)
    : FileLister<SimpleSortingMode>
{
    override fun listDir(
        path: String,
        sortingMode: SimpleSortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean
    )
        : List<FSItem>
    {
        val fileNamesArray = File(path).list()

        val fileList = mutableListOf<FSItem>()

        if (null != fileNamesArray) {
            for (name: String in fileNamesArray) {
                fileList.add(
                    convertDirToDirItem(
                        SimpleFSItem(
                            File(path, name)
                        )
                    )
                )
            }
        }

        return fileList.toList()
            .sortedWith(
                FSItemSortingComparator.create(sortingMode, reverseOrder, foldersFirst)
            )
    }
}