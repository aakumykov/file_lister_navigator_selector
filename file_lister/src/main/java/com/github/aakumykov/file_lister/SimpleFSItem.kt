package com.github.aakumykov.file_lister

open class SimpleFSItem(
    override val name: String,
    override val absolutePath: String,
    override val isDir: Boolean
) : FSItem {
    override fun toString(): String {
        return "$name ($absolutePath)"
    }
}