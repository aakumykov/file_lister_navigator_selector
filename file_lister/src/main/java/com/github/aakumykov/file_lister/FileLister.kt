package com.github.aakumykov.file_lister

interface FileLister {
    fun listDir(path: String): List<FSItem>
}
