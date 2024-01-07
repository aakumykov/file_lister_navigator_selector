package com.github.aakumykov.file_lister_navigator_selector.fs_item

import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import java.io.File

open class SimpleFSItem(
    override val name: String,
    override val absolutePath: String,
    override val parentPath: String,
    override val isDir: Boolean,
    override val cTime: Long
) : FSItem {

    constructor(file: File) : this(
        file.name,
        file.absolutePath,
        parentPathFor(file.absolutePath),
        file.isDirectory,
        file.lastModified()
    )

    override fun toString(): String {
        return "$name ($absolutePath)"
    }
}