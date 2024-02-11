package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator_dialog

import androidx.core.os.bundleOf
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.YandexDirCreator
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator

class YandexDirCreatorDialog : DirCreatorDialog() {

    override fun cloudDirCreator(): CloudDirCreator {
        // FIXME: решить проблему с null
        return YandexDirCreator(arguments?.getString(AUTH_TOKEN)!!)
    }

    override fun basePath(): String = "/"

    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        fun create(authToken: String): YandexDirCreatorDialog {
            return YandexDirCreatorDialog().apply {
                arguments = bundleOf(AUTH_TOKEN to authToken)
            }
        }
    }
}