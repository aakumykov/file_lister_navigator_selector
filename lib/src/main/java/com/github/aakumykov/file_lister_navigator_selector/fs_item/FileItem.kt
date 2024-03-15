package com.github.aakumykov.file_lister_navigator_selector.fs_item

import com.github.aakumykov.file_lister_navigator_selector.fs_item.utils.parentPathFor
import java.io.File

class FileItem(
    override val name: String,
    override val absolutePath: String,
    override val parentPath: String,
    override val mTime: Long,
    override val size: Long
) : SimpleFSItem(
    name,
    absolutePath,
    parentPath,
    false,
    mTime,
    size
) {
    constructor(file: File) : this(
        name = file.name,
        absolutePath = file.absolutePath,
        parentPath = parentPathFor(file.absolutePath),
        mTime = file.lastModified(),
        size = file.length()
    )

    override fun thisClassName(): String = FileItem::class.java.simpleName
}