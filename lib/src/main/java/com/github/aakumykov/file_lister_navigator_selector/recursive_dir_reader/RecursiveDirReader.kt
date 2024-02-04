package com.github.aakumykov.file_lister_navigator_selector.recursive_dir_reader

import android.net.Uri
import com.github.aakumykov.file_lister_navigator_selector.file_lister.FileLister
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import java.util.Date

class RecursiveDirReader(private val fileLister: FileLister) {

    private val list: MutableList<FileListItem> = mutableListOf()

    @Throws(FileLister.NotADirException::class)
    fun getRecursiveList(initialPath: String): List<FileListItem> {

        list.add(
            FileListItem(
                uri = Uri.parse(initialPath),
                parentPath = "",
                isDir = true,
                mTime = Date().time)
        )

        while(hasUnlistedDirs()) {

            getUnlistedDir()?.let { currentlyListedDir: FileListItem ->

                fileLister.listDir(currentlyListedDir.absolutePath).forEach { fsItem ->

                    val childItem = FileListItem(
                        name = fsItem.name,
                        absolutePath = fsItem.absolutePath,
                        parentPath = currentlyListedDir.absolutePath,
                        isDir = fsItem.isDir,
                        mTime = fsItem.mTime,
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


    class FileListItem (
        override val name: String,
        override val absolutePath: String,
        override val isDir: Boolean,
        override val mTime: Long,
        override val parentPath: String,
        val childIds: MutableList<String> = mutableListOf(),
        var isListed: Boolean = false,
    )
        : FSItem
    {
        constructor(uri: Uri, parentPath: String, isDir: Boolean, mTime: Long) : this(
            name = uri.lastPathSegment!!,
            absolutePath = uri.path!!,
            parentPath = parentPath,
            isDir = isDir,
            mTime = mTime
        )

        val id: String get() = absolutePath

        fun addChildId(id: String) {
            childIds.add(id)
        }

        override fun toString(): String {
            return FileListItem::class.java.simpleName + " { $name, (parentPath: '$parentPath', absolutePath: '$absolutePath') }"
        }
    }

    companion object {
        val TAG: String = RecursiveDirReader::class.java.simpleName
    }
}