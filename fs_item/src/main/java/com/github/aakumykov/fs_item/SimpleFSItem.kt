package com.github.aakumykov.fs_item

import java.io.File

open class SimpleFSItem(
    override val name: String,
    override val absolutePath: String,
    override val isDir: Boolean,
    override val cTime: Long
) : FSItem {

    constructor(file: File) : this(file.name, file.absolutePath, file.isDirectory, file.lastModified())

    override fun toString(): String {
        return "$name ($absolutePath)"
    }
}