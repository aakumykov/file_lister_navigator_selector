package com.example.yandex_disk_file_lister

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.file_lister.ParentDirItem

class YandexDiskFileLister(authToken: String,
                           private val dirSeparator: String = FileLister.DS
) : FileLister {

    private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient(authToken)
    }

    override fun listDir(path: String): List<FSItem> {
        return yandexDiskClient.listDir(path)
    }


    /*private val yandexDiskClient: FileListerYandexDiskClient by lazy {
        FileListerYandexDiskClient().apply {
            setAuthToken(authToken)
        }
    }


    override fun openAndListDir(dirItem: DirItem): List<FSItem> {
        return when (dirItem) {
            is RootDirItem -> openAndListRootDir()
            is ParentDirItem -> goToParentDir()
            else -> goToChildDir(dirItem.name)
        }
    }


    override fun getCurrentPath(): String = currentPath


    override fun loadMore(loadMoreItem: LoadMoreItem): List<FSItem> {
        TODO("Не реализовано")
    }


    private fun goToParentDir(): List<FSItem> {
        currentPath = currentPath.replace(PARENT_DIR_PATTERN.toRegex(), "")
        return listPathCurrentPath()
    }


    private fun goToChildDir(dirName: String): List<FSItem> {
        currentPath = (currentPath + DS + dirName).replace(Regex("(${DS})+"), DS)
        return listPathCurrentPath()
    }


    // FIXME: переименовать в listCurrentPath()
    private fun listPathCurrentPath(): List<FSItem> {
        val list = yandexDiskClient.listDir(currentPath)
        return when(isDirMode) {
            true -> list.filterIsInstance<DirItem>()
            false -> list
        }
    }*/
}