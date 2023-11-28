package com.github.aakumykov.fs_item

class ParentDirItem : SimpleFSItem(
    name = FSItem.PARENT_DIR_NAME,
    absolutePath = FSItem.PARENT_DIR_PATH,
    isDir = true
)