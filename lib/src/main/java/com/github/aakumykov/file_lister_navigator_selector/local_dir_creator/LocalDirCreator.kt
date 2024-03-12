package com.github.aakumykov.file_lister_navigator_selector.local_dir_creator

import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import java.io.File
import java.io.IOException

class LocalDirCreator : DirCreator {

    @Throws(IOException::class)
    override fun makeDir(path: String): Boolean {
        return File(path).mkdir()
    }
}