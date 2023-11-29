package com.github.aakumykov.recursive_dir_reader

import com.github.aakumykov.file_lister.FileLister
import java.io.File

class RecursiveDirReader(private val fileLister: FileLister) {

    private val list: MutableList<FileListItem> = mutableListOf()

    fun getRecursiveList(initialPath: String): List<FileListItem> {

        list.add(FileListItem(initialPath))

        while(hasUnlistedDirs()) {

            getUnlistedDir()?.let { currentlyListedDir: FileListItem ->

                fileLister.listDir(currentlyListedDir.absolutePath).forEach { fsItem ->

                    val childItem = FileListItem(
                        file = File(currentlyListedDir, fsItem.name),
                        parentDir = currentlyListedDir
                    )

                    currentlyListedDir.addChildId(childItem.id)

                    list.add(childItem)
                }

                currentlyListedDir.isListed = true
            }
        }

        return list
    }

    private fun hasUnlistedDirs(): Boolean {
        return null != getUnlistedDir()
    }

    private fun getUnlistedDir(): FileListItem? {
        return list.firstOrNull { item ->
            val isDir = item.isDirectory
            val isListed = item.isListed
            isDir && !isListed
//            (item.isDirectory && !item.isListed)
        }
    }


    // TODO: унаследовать FileListItem от FSItem?
    class FileListItem (
        absolutePath: String,
        val parentId: String? = null,
        val childIds: MutableList<String> = mutableListOf(),
        var isListed: Boolean = false
    )
        : File(absolutePath)
    {
        constructor(absolutePath: String, isDir: Boolean) : this(absolutePath = absolutePath, )
        constructor(file: File, parentDir: FileListItem) : this(absolutePath = file.absolutePath, parentId = parentDir.id)

        val id: String get() = absolutePath

        fun addChildId(id: String) {
            childIds.add(id)
        }
    }

    companion object {
        val TAG: String = RecursiveDirReader::class.java.simpleName
    }
}