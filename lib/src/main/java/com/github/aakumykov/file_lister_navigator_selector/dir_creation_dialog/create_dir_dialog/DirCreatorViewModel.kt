package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.aakumykov.cloud_writer.CloudWriter
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.CloudDirCreator
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DirCreatorViewModel(private val cloudDirCreator: CloudDirCreator) : ViewModel() {

    private val _operationState: MutableLiveData<SimpleOperationState> = MutableLiveData(
        SimpleOperationState.Idle
    )
    val operationState: LiveData<SimpleOperationState> = _operationState


    fun createDir(name: String, basePath: String) {

        if (name.isEmpty() || name.isBlank()) {
            setOpState(SimpleOperationState.BadNameError(R.string.error_cannot_be_empty))
            return
        }

        viewModelScope.launch {
            try {
                setOpState(SimpleOperationState.Busy)
                withContext(Dispatchers.IO) { cloudDirCreator.createDir(name, basePath) }
                setOpState(SimpleOperationState.Success)
            }
            catch (t: Throwable) {
                val errorMessage = ExceptionUtils.getErrorMessage(t)
                Log.e(TAG, errorMessage)
                setOpState(when (t) {
                        is CloudWriter.AlreadyExistsException -> SimpleOperationState.AlreadyExistsError(R.string.error_already_exists)
                        else -> SimpleOperationState.CommonError(errorMessage)
                })
            }
        }
    }


    private fun setOpState(state: SimpleOperationState) {
        _operationState.value = state
    }


    companion object {
        val TAG: String = DirCreatorViewModel::class.java.simpleName

        fun provideFactory(cloudDirCreator: CloudDirCreator): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DirCreatorViewModel(cloudDirCreator) as T
                }
            }
    }
}