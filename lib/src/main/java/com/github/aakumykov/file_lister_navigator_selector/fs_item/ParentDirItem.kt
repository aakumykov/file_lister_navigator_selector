package com.github.aakumykov.file_lister_navigator_selector.fs_item

/**
 * Класс, использующийся для добавления в список элемента "..", указывающего на родительский каталог.
 */
class ParentDirItem () : DirItem(
    name = FSItem.PARENT_DIR_NAME,
    absolutePath = FSItem.PARENT_DIR_PATH,
    parentPath = FSItem.NO_PARENT_PATH,
    cTime = -1L
)