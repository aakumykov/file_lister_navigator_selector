package com.github.aakumykov.file_lister_navigator_selector.fragments.selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.FragmentSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.extensions.showToast
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelectorDialog
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.local_file_selector.LocalFileSelectorDialog
import com.github.aakumykov.storage_access_helper.storage_access_helper.StorageAccessHelper

class SelectorFragment : Fragment(R.layout.fragment_selector), FileSelectorDialog.Callback,
    StorageAccessHelper.ResultCallback {

    private var _binding: FragmentSelectorBinding? = null
    private val binding: FragmentSelectorBinding get() = _binding!!
    private lateinit var storageAccessHelper: StorageAccessHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageAccessHelper = StorageAccessHelper.create(requireActivity(), this)

        _binding = FragmentSelectorBinding.bind(view)

        binding.selectButton.setOnClickListener { storageAccessHelper.requestReadingAccess() }
    }

    override fun onStorageAccessResult(grantedMode: StorageAccessHelper.StorageAccessMode) {
        when(grantedMode) {
            StorageAccessHelper.StorageAccessMode.READING_YES -> showFileSelector()
            StorageAccessHelper.StorageAccessMode.FULL_YES -> showFileSelector()
            else -> showToast(R.string.no_storage_read_access)
        }
    }

    private fun showFileSelector() {

        val fileSelector = LocalFileSelectorDialog.create(
                callback = this,
                isMultipleSelectionMode = true
            ).show(childFragmentManager)

//                YandexDiskFileSelector.create(yandexAuthToken!!)

        fileSelector.setCallback(this)
    }

    override fun onDestroyView() {
        FileSelectorDialog.find(LocalFileSelectorDialog.TAG, childFragmentManager)?.unsetCallback()
        _binding = null
        super.onDestroyView()
    }

    override fun onFilesSelected(selectedItemsList: List<FSItem>) {
        showToast(selectedItemsList.joinToString(separator = "\n"))
    }

    companion object {
        val TAG: String = SelectorFragment::class.java.simpleName
        fun create(): SelectorFragment {
            return SelectorFragment()
        }
    }
}