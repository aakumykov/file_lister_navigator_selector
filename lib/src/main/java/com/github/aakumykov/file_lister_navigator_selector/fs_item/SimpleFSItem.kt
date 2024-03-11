package com.github.aakumykov.file_lister_navigator_selector.fs_item

import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import java.io.File

@Deprecated("Переименовать в BasicFSItem")
open class SimpleFSItem(
    override val name: String,
    override val absolutePath: String,
    override val parentPath: String,
    override val isDir: Boolean,
    override val mTime: Long,
    override val size: Long
) : FSItem {

    constructor(file: File) : this(
        name = file.name,
        absolutePath = file.absolutePath,
        parentPath = parentPathFor(file.absolutePath),
        isDir = file.isDirectory,
        mTime = file.lastModified(),
        size = file.length()
    )

    override fun toString(): String {
        return "$name ($absolutePath)"
    }
}