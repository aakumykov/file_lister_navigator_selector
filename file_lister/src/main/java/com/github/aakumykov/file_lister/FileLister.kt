package com.github.aakumykov.file_lister

interface FileLister {
    fun listRootDir(): List<FSItem>
    fun listDir(path: String): List<FSItem>
}
