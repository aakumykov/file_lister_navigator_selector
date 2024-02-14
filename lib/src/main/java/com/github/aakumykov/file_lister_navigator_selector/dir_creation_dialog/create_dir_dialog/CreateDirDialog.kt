package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogDirCreatorBinding
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator.CloudDirCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class CreateDirDialog : DialogFragment(R.layout.dialog_dir_creator) {

    private var _binding: DialogDirCreatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DirCreatorViewModel by viewModels { DirCreatorViewModel.provideFactory(cloudDirCreator()) }

    val dirCreationEventLiveData: LiveData<String> get() = viewModel.dirCreationEventLiveData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogDirCreatorBinding.bind(view)

        binding.dialogHeaderInclude.titleView.setText(R.string.create_dir_title)
        binding.dialogHeaderInclude.closeButton.setOnClickListener { dismiss() }

        binding.cancelButton.setOnClickListener { dismiss() }
        binding.confirmButton.setOnClickListener { startCreatingDir() }

        viewModel.operationState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is SimpleOperationState.Idle -> showIdleState()
                is SimpleOperationState.Success -> processSuccessState()
                is SimpleOperationState.Busy -> showBusyState()
                is SimpleOperationState.BadNameError -> showNameErrorState(state)
                is SimpleOperationState.AlreadyExistsError -> showCommonErrorState(state.errorMessageRes)
                is SimpleOperationState.CommonError -> showCommonErrorState(state)
                null -> { Log.w(TAG, "Simple operation state is null.") }
            }
        }
    }

    abstract fun cloudDirCreator(): CloudDirCreator

    abstract fun basePath(): String


    private fun startCreatingDir() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.createDir(dirName(), basePath())
            }
        }
    }


    private fun showIdleState() {
        enableForm()
        hideProgressBar()
        hideError()
    }

    private fun showBusyState() {
        disableForm()
        hideError()
        showProgressBar()
    }

    private fun showCommonErrorState(state: SimpleOperationState.CommonError) {
        hideProgressBar()
        enableForm()
        showError(state.errorMessage)
    }

    private fun showCommonErrorState(@StringRes errorMessageRes: Int) {
        hideProgressBar()
        enableForm()
        showError(getString(errorMessageRes))
    }

    private fun showNameErrorState(state: SimpleOperationState.BadNameError) {
        hideProgressBar()
        enableForm()
        binding.nameInput.error = getString(state.errorMessageRes)
    }


    private fun processSuccessState() {
        Toast.makeText(requireContext(), getString(R.string.dir_created, dirName()), Toast.LENGTH_SHORT).show()
        dismiss()
    }


    private fun enableForm() {
        binding.confirmButton.isEnabled = true
        binding.nameInput.isEnabled = true
    }

    private fun disableForm() {
        binding.confirmButton.isEnabled = false
        binding.nameInput.isEnabled = false
    }

    private fun showProgressBar() {
        binding.dialogFooterInclude.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.dialogFooterInclude.progressBar.visibility = View.GONE
    }

    private fun showError(errorMessage: String) {
        binding.dialogFooterInclude.errorView.apply {
            text = errorMessage
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.dialogFooterInclude.errorView.apply {
            text = ""
            visibility = View.GONE
        }
    }


    private fun dirName() = binding.nameInput.text.toString()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        val TAG: String = CreateDirDialog::class.java.name

        fun find(fragmentManager: FragmentManager): CreateDirDialog? {
            return fragmentManager.findFragmentByTag(TAG).let {
                when(it) {
                    is CreateDirDialog -> it
                    else -> null
                }
            }
        }
    }
}