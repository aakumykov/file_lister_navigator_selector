package com.github.aakumykov.file_lister

class ParentDirItem : SimpleFSItem(
    name = FileLister.PARENT_DIR_NAME,
    absolutePath = FileLister.PARENT_DIR_PATH,
    isDir = true
)