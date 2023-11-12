package com.github.aakumykov.file_lister

open class SimpleFSItem(
    override val name: String,
    override val path: String,
    override val isDir: Boolean
) : FSItem