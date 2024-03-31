package com.github.aakumykov.file_lister_navigator_selector.file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

abstract class BasicFileLister : FileLister<SortingMode> {

    fun categorizeFSItems(list: List<FSItem>): List<FSItem> {
        return list.map { fsItem ->
            if (fsItem.isDir) DirItem(fsItem)
            else fsItem
        }
    }
}