package com.github.aakumykov.file_lister_navigator_selector.fs_item

open class DirItem(
    name: String,
    absolutePath: String,
    parentPath: String,
    cTime: Long
) :
    SimpleFSItem(
        name = name,
        absolutePath = absolutePath,
        parentPath = parentPath,
        isDir = true,
        cTime = cTime
    )