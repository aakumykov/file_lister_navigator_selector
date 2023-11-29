package com.github.aakumykov.recursive_dir_reader

import android.net.Uri
import com.github.aakumykov.file_lister.FileLister
import com.github.aakumykov.fs_item.FSItem
import java.io.File

class RecursiveDirReader(private val fileLister: FileLister) {

    private val list: MutableList<FileListItem> = mutableListOf()

    fun getRecursiveList(initialPath: String): List<FileListItem> {

        list.add(FileListItem(Uri.parse(initialPath), true))

        while(hasUnlistedDirs()) {

            getUnlistedDir()?.let { currentlyListedDir: FileListItem ->

                fileLister.listDir(currentlyListedDir.absolutePath).forEach { fsItem ->

                    val childItem = FileListItem(
                        name = fsItem.name,
                        absolutePath = fsItem.absolutePath,
                        isDir = fsItem.isDir,
                        parentId = currentlyListedDir.id
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
            val isDir = item.isDir
            val isListed = item.isListed
            isDir && !isListed
//            (item.isDirectory && !item.isListed)
        }
    }


    // TODO: унаследовать FileListItem от FSItem?
    class FileListItem (
        override val name: String,
        override val absolutePath: String,
        override val isDir: Boolean,
        val parentId: String? = null,
        val childIds: MutableList<String> = mutableListOf(),
        var isListed: Boolean = false,
    )
        : FSItem
    {
        constructor(uri: Uri, isDir: Boolean) : this(
            name = uri.lastPathSegment!!,
            absolutePath = uri.path!!,
            isDir = isDir
        )

        val id: String get() = absolutePath

        fun addChildId(id: String) {
            childIds.add(id)
        }
    }

    companion object {
        val TAG: String = RecursiveDirReader::class.java.simpleName
    }
}