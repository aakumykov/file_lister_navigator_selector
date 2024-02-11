package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator_dialog

import androidx.annotation.StringRes

sealed class SimpleOperationState {

    data object Idle : SimpleOperationState()
    data object Busy : SimpleOperationState()
    data object Success : SimpleOperationState()

    class CommonError(val errorMessage: String) : SimpleOperationState()
    class NameError(@StringRes val errorMessageRes: Int) : SimpleOperationState()
}