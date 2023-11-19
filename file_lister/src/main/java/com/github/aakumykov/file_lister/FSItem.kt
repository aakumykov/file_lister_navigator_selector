package com.github.aakumykov.file_lister

interface FSItem {
    val name: String
    val absolutePath: String
    val isDir: Boolean
}