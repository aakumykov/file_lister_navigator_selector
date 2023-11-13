package com.github.aakumykov.file_lister

interface FSItem {
    val name: String
    val path: String
    val isDir: Boolean
}