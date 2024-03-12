package com.github.aakumykov.file_lister_navigator_selector.dir_creator

import java.io.IOException

interface DirCreator {

    @Throws(IOException::class)
    fun makeDir(path: String): Boolean
}