package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_selector

import android.os.Bundle
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator.YandexDiskDirCreator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.FileListerYandexDiskClient
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator.YandexDiskFileExplorer

class YandexDiskFileSelector : FileSelector() {

    override fun dirCreatorDialog(basePath: String): DirCreatorDialog {
        TODO("Not yet implemented")
    }

    private var _fileExplorer: FileExplorer? = null

    override fun fileExplorer(): FileExplorer {
        if (null == _fileExplorer) {

            val authToken = arguments?.getString(AUTH_TOKEN)

            if (authToken.isNullOrEmpty())
                throw IllegalArgumentException("Auth token is null or empty")
            else {
                val yandexDiskClient = FileListerYandexDiskClient(authToken)
                _fileExplorer = YandexDiskFileExplorer(
                        yandexDiskFileLister = YandexDiskFileLister(authToken),
                        yandexDiskDirCreator = YandexDiskDirCreator(yandexDiskClient),
                        isDirMode = isDirMode
                )
            }
        }

        return _fileExplorer!!
    }

    override fun defaultStartPath(): String = "/"

    
    // TODO: общий метод для создания этих диалогов
    companion object {
        val TAG: String = YandexDiskFileSelector::class.java.simpleName

        fun create(authToken: String,
                   startPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false
        ) : YandexDiskFileSelector
        {
            return YandexDiskFileSelector().apply {
                arguments = Bundle().apply {
                    putString(AUTH_TOKEN, authToken)
                    putString(START_PATH, startPath)
                    putBoolean(IS_DIR_MODE, isDirMode)
                    putBoolean(IS_MULTIPLE_SELECTION_MODE, isMultipleSelectionMode)
                }
            }
        }
    }
}