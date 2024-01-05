package com.github.aakumykov.file_lister_navigator_selector.fs_item

open class DirItem(name: String, absolutePath: String, cTime: Long)
    : SimpleFSItem(name = name, absolutePath = absolutePath, isDir = true, cTime = cTime)