package com.github.aakumykov.file_lister

class ParentDirItem : SimpleFSItem(
    name = FSItem.PARENT_DIR_NAME,
    absolutePath = FSItem.PARENT_DIR_PATH,
    isDir = true
)