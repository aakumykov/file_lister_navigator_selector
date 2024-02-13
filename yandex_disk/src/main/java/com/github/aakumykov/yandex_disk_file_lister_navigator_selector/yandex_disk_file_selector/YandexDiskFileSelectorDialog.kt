package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_selector

import android.os.Bundle
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog.CreateDirDialog
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelectorDialog
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator_dialog.YandexCreateDirDialog
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator.YandexDiskFileExplorer

class YandexDiskFileSelectorDialog: FileSelectorDialog() {

    private var _fileExplorer: FileExplorer? = null
    private var authToken: String? = null


    override fun fileExplorer(): FileExplorer {
        if (null == _fileExplorer) {

            authToken = arguments?.getString(AUTH_TOKEN)

            if (authToken.isNullOrEmpty())
                throw IllegalArgumentException("Auth token is null or empty")
            else
                _fileExplorer =
                    YandexDiskFileExplorer(
                        YandexDiskFileLister(authToken!!),
                        isDirMode = isDirMode
                    )
        }

        return _fileExplorer!!
    }

    override fun defaultStartPath(): String = "/"

    override fun createDirDialog(): CreateDirDialog = YandexCreateDirDialog.create(authToken!!)


    // TODO: общий метод для создания этих диалогов
    companion object {
        val TAG: String = YandexDiskFileSelectorDialog::class.java.simpleName

        fun create(authToken: String,
                   startPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false
        ) : YandexDiskFileSelectorDialog
        {
            return YandexDiskFileSelectorDialog().apply {
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