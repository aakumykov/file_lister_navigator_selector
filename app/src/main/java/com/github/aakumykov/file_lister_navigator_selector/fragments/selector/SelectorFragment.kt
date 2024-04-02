package com.github.aakumykov.file_lister_navigator_selector.fragments.selector

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.file_lister_navigator_selector.LocalFileSelector2.LocalFileSelector2
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.FragmentSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.extensions.showToast
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector
import com.github.aakumykov.file_lister_navigator_selector.file_selector2.FileSelector2
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.local_file_selector.LocalFileSelector
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.google.gson.Gson

class SelectorFragment : Fragment(R.layout.fragment_selector), FileSelector.Callback {

    private var _binding: FragmentSelectorBinding? = null
    private val binding: FragmentSelectorBinding get() = _binding!!
    private lateinit var storageAccessHelper: StorageAccessHelper
    private val gson by lazy { Gson() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSelectorBinding.bind(view)

        storageAccessHelper = StorageAccessHelper.create(this)

        binding.selectButton.setOnClickListener { onSelectButtonClicked() }

        if (null == savedInstanceState)
            onSelectButtonClicked()
    }

    private fun onSelectButtonClicked() {
        storageAccessHelper.requestReadAccess {
            showFileSelector()
        }
    }

    private fun showFileSelector() {

        childFragmentManager.setFragmentResultListener(FileSelector2.ITEMS_SELECTION, viewLifecycleOwner)
            { requestKey, result ->
                if (FileSelector2.ITEMS_SELECTION == requestKey) {
                    result.getStringArrayList(FileSelector2.SELECTED_ITEMS_LIST)?.also { listOfJSON ->
                        val fsItemList: List<FSItem> = listOfJSON.map {  jsonFSItem ->
                            return@map gson.fromJson(jsonFSItem, SimpleFSItem::class.java)
                        }
                        Log.d(TAG, fsItemList.toString())
                        showToast("Выбрано:\n${fsItemList.map { it.name }.joinToString("\n")}")
                    }
                }
            }

        LocalFileSelector2().show(childFragmentManager, LocalFileSelector2.TAG)
    }

    override fun onDestroyView() {
        FileSelector.find(LocalFileSelector.TAG, childFragmentManager)?.unsetCallback()
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