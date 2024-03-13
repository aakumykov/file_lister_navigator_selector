package com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogDirCreatorBinding
import com.github.aakumykov.file_lister_navigator_selector.dir_creator.DirCreator
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils

abstract class DirCreatorDialog : DialogFragment(R.layout.dialog_dir_creator) {

    private var _binding: DialogDirCreatorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DirCreatorViewModel by viewModels { DirCreatorViewModel.factory(dirCreator()) }

    abstract fun dirCreator(): DirCreator


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogDirCreatorBinding.bind(view)
        binding.dialogCloseButton.setOnClickListener { dismiss() }
        binding.confirmButton.setOnClickListener { onCreateButtonClicked() }

        viewModel.isOperationSuccess.observe(viewLifecycleOwner, ::onOperationSuccessChanged)
        viewModel.errorMsg.observe(viewLifecycleOwner, ::onErrorMessageChanged)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun onOperationSuccessChanged(isSuccess: Boolean?) {
        if (null != isSuccess) {
            if (isSuccess) dismiss()
            else hideProgressBar()
        }
    }

    private fun onErrorMessageChanged(e: Exception?) {
        hideProgressBar()
        enableForm()
        showError(e)
    }


    private fun onCreateButtonClicked() {
        showProgressBar()
        disableForm()
        hideError()

        val basePath = arguments?.getString(BASE_PATH)
        val dirName = binding.dirNameInput.text.toString()

        viewModel.createDir(basePath + FSItem.DS + dirName, Handler(Looper.getMainLooper()))
    }


    private fun showError(e: Exception?) {
        if (null != e) {
            ExceptionUtils.getErrorMessage(e).also {
                binding.dirNameInput.error = it
                Log.e(TAG, it, e)
            }
        }
    }
    
    private fun hideError() {
        binding.dirNameInput.error = null
    }
    
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun enableForm() {
        binding.dirNameInput.isEnabled = true
    }

    private fun disableForm() {
        binding.dirNameInput.isEnabled = false
    }

    
    companion object {
        val TAG: String = DirCreatorDialog::class.java.simpleName
        const val BASE_PATH = "BASE_PATH"
        fun argumentsWithBasePath(basePath: String) = bundleOf(BASE_PATH to basePath)
    }
}