package com.github.aakumykov.file_lister_navigator_selector_demo.fragments.selector

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelectorFragment
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.local_file_selector.LocalFileSelectorFragment
import com.github.aakumykov.file_lister_navigator_selector_demo.R
import com.github.aakumykov.file_lister_navigator_selector_demo.databinding.FragmentLocalBinding
import com.github.aakumykov.file_lister_navigator_selector_demo.extensions.showToast
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.google.gson.Gson

class LocalFragment : Fragment(R.layout.fragment_local) {

    private var _binding: FragmentLocalBinding? = null
    private val binding: FragmentLocalBinding get() = _binding!!
    private lateinit var storageAccessHelper: StorageAccessHelper
    private val gson by lazy { Gson() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLocalBinding.bind(view)

        storageAccessHelper = StorageAccessHelper.create(this)

        binding.selectButton.setOnClickListener { onSelectButtonClicked() }

        prepareFragmentResultListener()

        if (null == savedInstanceState)
            onSelectButtonClicked()
    }

    private fun onSelectButtonClicked() {
        storageAccessHelper.requestReadAccess {
            showFileSelector()
        }
    }

    private fun showFileSelector() {
        LocalFileSelectorFragment.create(
                isDirSelectionMode = binding.dirModeSwitch.isChecked,
                isMultipleSelectionMode = binding.multipleModeSwitch.isChecked)
            .show(childFragmentManager, FileSelectorFragment.TAG)
    }

    private fun prepareFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(FileSelectorFragment.REQUEST_ITEMS_SELECTION, viewLifecycleOwner)
        { requestKey, result ->
            if (FileSelectorFragment.REQUEST_ITEMS_SELECTION == requestKey) {
                FileSelectorFragment.extractSelectionResult(result)?.also { fsItemList ->
                    Log.d(TAG, fsItemList.toString())
                    displaySelectedItems(fsItemList)
                } ?: showToast("Ошибка обработки результата выбора файлов.")
            }
        }
    }

    private fun displaySelectedItems(fsItemList: List<FSItem>) {
        binding.infoView.text = "Выбрано:\n${fsItemList.map { it.name }.joinToString("\n")}"
    }

    override fun onDestroyView() {
//        FileSelectorFragment.find(LocalFileSelectorFragment.TAG, childFragmentManager)?.unsetCallback()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        val TAG: String = LocalFragment::class.java.simpleName
        fun create(): LocalFragment {
            return LocalFragment()
        }
    }
}