package com.github.aakumykov.file_lister_navigator_selector.fs_item

interface FSItem {

    val name: String
    val absolutePath: String
    val isDir: Boolean
    val cTime: Long

    companion object {
        const val DS: String = "/"
        const val PARENT_DIR_NAME: String = ".."
        const val PARENT_DIR_PATH: String = ".."
    }
}