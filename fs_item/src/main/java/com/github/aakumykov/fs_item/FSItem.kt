package com.github.aakumykov.fs_item

interface FSItem {

    val name: String
    val absolutePath: String
    val isDir: Boolean

    companion object {
        const val DS: String = "/"
        const val PARENT_DIR_NAME: String = ".."
        const val PARENT_DIR_PATH: String = ".."
    }
}