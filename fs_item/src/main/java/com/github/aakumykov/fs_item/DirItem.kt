package com.github.aakumykov.fs_item

open class DirItem(name: String, absolutePath: String)
    : SimpleFSItem(name = name, absolutePath = absolutePath, isDir = true)