package com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.cloud_dir_creator_dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogDirCratorBinding
import com.github.aakumykov.kotlin_playground.cloud_dir_creator.CloudDirCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DirCreatorDialog : DialogFragment(R.layout.dialog_dir_crator) {

    private var _binding: DialogDirCratorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DirCreatorViewModel by viewModels {
        DirCreatorViewModel.provideFactory(cloudDirCreator())
    }

    abstract fun cloudDirCreator(): CloudDirCreator
    abstract fun basePath(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogDirCratorBinding.bind(view)

        binding.cancelButton.setOnClickListener { dismiss() }
        binding.confirmButton.setOnClickListener { startCreatingDir() }

        viewModel.operationState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is SimpleOperationState.Idle -> showIdleState()
                is SimpleOperationState.Success -> processSuccessState()
                is SimpleOperationState.Busy -> showBusyState()
                is SimpleOperationState.NameError -> showNameErrorState(state)
                is SimpleOperationState.CommonError -> showCommonErrorState(state)
                null -> { Log.w(TAG, "Simple operation state is null.") }
            }
        }
    }

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
        binding.commonErrorView.apply {
            text = state.errorMessage
            visibility = View.VISIBLE
        }
    }

    private fun showNameErrorState(state: SimpleOperationState.NameError) {
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
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun hideError() {
        binding.commonErrorView.apply {
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
        val TAG: String = DirCreatorDialog::class.java.name
    }
}