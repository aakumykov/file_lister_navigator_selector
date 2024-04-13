package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_selector

import android.os.Bundle
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelectorFragment
import com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier.SimpleSortingInfoSupplier
import com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier.SortingInfoSupplier
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SimpleSortingModeTranslator
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator.SortingModeTranslator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator.YandexDiskDirCreator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator_dialog.YandexDiskDirCreatorDialog
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.FileListerYandexDiskClient
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_file_lister.YandexDiskFileLister
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_fs_navigator.YandexDiskFileExplorer

// TODO: внедрять зависимости

class YandexDiskFileSelectorFragment : FileSelectorFragment<SimpleSortingMode>() {

    override fun createDirCreatorDialog(basePath: String): DirCreatorDialog {
        // TODO: как быть с "!!" ?
        return YandexDiskDirCreatorDialog.create(basePath, authToken()!!)
    }

    override fun createSortingInfoSupplier(): SortingInfoSupplier<SimpleSortingMode> {
        return SimpleSortingInfoSupplier()
    }

    override fun createSortingModeTranslator(): SortingModeTranslator<SimpleSortingMode> {
        return SimpleSortingModeTranslator(resources)
    }

    override fun defaultSortingMode(): SimpleSortingMode {
        return SimpleSortingMode.NAME
    }

    override fun defaultReverseMode(): Boolean = false

    private var _fileExplorer: FileExplorer<SimpleSortingMode>? = null

    override fun getDefaultInitialPath(): String = "/"

    override fun createFileExplorer(): FileExplorer<SimpleSortingMode> {
        if (null == _fileExplorer) {

            val authToken = authToken()

            if (authToken.isNullOrEmpty())
                throw IllegalArgumentException("Auth token is null or empty")
            else {
                val yandexDiskClient = FileListerYandexDiskClient(authToken)
                _fileExplorer = YandexDiskFileExplorer(
                        yandexDiskFileLister = YandexDiskFileLister(authToken),
                        yandexDiskDirCreator = YandexDiskDirCreator(yandexDiskClient),
                        initialPath = "/",
                        isDirMode = false,
                )
            }
        }

        return _fileExplorer!!
    }

    override fun requestWriteAccess(
        onWriteAccessGranted: () -> Unit,
        onWriteAccessRejected: (errorMsg: String?) -> Unit
    ) {
        onWriteAccessGranted()
    }


    private fun authToken(): String? = arguments?.getString(AUTH_TOKEN)


    // TODO: общий метод для создания этих диалогов
    companion object {
        val TAG: String = YandexDiskFileSelectorFragment::class.java.simpleName

        fun create(authToken: String,
                   initialPath: String? = null,
                   isMultipleSelectionMode: Boolean = false,
                   isDirMode: Boolean = false
        ) : YandexDiskFileSelectorFragment
        {
            return YandexDiskFileSelectorFragment().apply {
                arguments = Bundle().apply {
                    putString(AUTH_TOKEN, authToken)
                    putString(INITIAL_PATH, initialPath)
//                    putBoolean(IS_DIR_MODE, isDirMode)
//                    putBoolean(IS_MULTIPLE_SELECTION_MODE, isMultipleSelectionMode)
                }
            }
        }
    }
}