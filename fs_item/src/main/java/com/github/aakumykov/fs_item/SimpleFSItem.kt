package com.github.aakumykov.fs_item

open class SimpleFSItem(
    override val name: String,
    override val absolutePath: String,
    override val isDir: Boolean,
    override val cTime: Long
) : FSItem {
    override fun toString(): String {
        return "$name ($absolutePath)"
    }
}