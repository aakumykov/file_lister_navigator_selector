package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister

import com.github.aakumykov.cloud_reader.YandexCloudReader
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class YandexDiskFileLister @AssistedInject constructor(
    @Assisted private val authToken: String,
    private val yandexCloudReader: YandexCloudReader
)
    : FileLister<SimpleSortingMode>
{
    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    /**
     * Параметр foldersFirst не имеет эффекта.
     */
    override fun listDir(
        path: String,
        sortingMode: SimpleSortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean,
        dirMode: Boolean
    )
        : List<FSItem>
    {
        return yandexDiskClient
            .listDir(path, sortingMode, reverseOrder)
            .filter {
                // TODO: вынести этот фильтр в единое место
                if (dirMode) it.isDir
                else true
            }
            .map { convertDirToDirItem(it) }
    }

    override suspend fun fileExists(path: String): Result<Boolean> = yandexCloudReader.fileExists(path)
}
