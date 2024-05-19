package com.github.aakumykov.file_lister_navigator_selector.local_file_lister

import com.github.aakumykov.cloud_reader.local_cloud_reader.LocalCloudReader
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.FSItemSortingComparator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

class LocalFileLister @AssistedInject constructor(
    @Assisted private val dummyAuthToken: String,
    private val localCloudReader: LocalCloudReader
)
    : FileLister<SimpleSortingMode>
{
    override fun listDir(
        path: String,
        sortingMode: SimpleSortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean,
        dirMode: Boolean
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

        return fileList
            .toList()
            .filter {
                // TODO: вынести этот фильтр в единое место
                if (dirMode) it.isDir
                else true
            }
            .sortedWith(
                FSItemSortingComparator.create(sortingMode, reverseOrder, foldersFirst)
            )
    }


    override suspend fun fileExists(path: String): Result<Boolean> = localCloudReader.fileExists(path)
}