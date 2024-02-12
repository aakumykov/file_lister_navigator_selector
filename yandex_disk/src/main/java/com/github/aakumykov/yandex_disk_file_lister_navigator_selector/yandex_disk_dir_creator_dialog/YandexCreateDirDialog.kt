package com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator_dialog

import androidx.core.os.bundleOf
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog.CreateDirDialog
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator
import com.github.aakumykov.yandex_disk_file_lister_navigator_selector.yandex_disk_dir_creator.YandexDirCreator

class YandexCreateDirDialog : CreateDirDialog() {

    override fun cloudDirCreator(): CloudDirCreator {
        // FIXME: решить проблему с null
        return YandexDirCreator(
            arguments?.getString(AUTH_TOKEN)!!
        )
    }

    override fun basePath(): String = "/"

    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        fun create(authToken: String): YandexCreateDirDialog {
            return YandexCreateDirDialog().apply {
                arguments = bundleOf(AUTH_TOKEN to authToken)
            }
        }
    }
}