package com.github.aakumykov.yandex_disk_file_selector

import android.os.Bundle
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_selector.FileSelector
import com.github.aakumykov.yandex_disk_file_explorer.YandexDiskFileExplorer
import com.github.aakumykov.yandex_disk_file_lister.YandexDiskFileLister
import java.lang.IllegalArgumentException

class YandexDiskFileSelector : FileSelector() {

    private var _fileExplorer: FileExplorer? = null

    override fun fileExplorer(): FileExplorer {
        if (null == _fileExplorer) {

            val authToken = arguments?.getString(AUTH_TOKEN)

            if (authToken.isNullOrEmpty())
                throw IllegalArgumentException("Auth token is null or empty")
            else
                _fileExplorer = YandexDiskFileExplorer(
                    YandexDiskFileLister(authToken),
                    isDirMode = isDirMode
                )
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