package com.github.aakumykov.file_lister_navigator_selector.fs_item

open class DirItem(
    name: String,
    absolutePath: String,
    parentPath: String,
    mTime: Long
) :
    SimpleFSItem(
        name = name,
        absolutePath = absolutePath,
        parentPath = parentPath,
        isDir = true,
        mTime = mTime,
        size = 0L
    ) {

    constructor(fsItem: FSItem) : this(fsItem.name, fsItem.absolutePath, fsItem.parentPath, fsItem.mTime)

    override fun toString(): String = TAG + " { ${nameAndPath()} }"

    companion object {
        val TAG: String = DirItem::class.java.simpleName
        fun fromPath(path: String): DirItem {
            return path.split(FSItem.DS).let { pathParts ->
                DirItem(
                    pathParts.last(),
                    path,
                    pathParts.subList(0, pathParts.size-1).joinToString(FSItem.DS),
                    0L
                )
            }
        }
    }
}