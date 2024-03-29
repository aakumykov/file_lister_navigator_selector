package com.github.aakumykov.file_lister_navigator_selector.file_selector2

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister_navigator_selector.FileListAdapter
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogFileSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils

abstract class FileSelector2 : DialogFragment(R.layout.dialog_file_selector) {

    private var isFirstRun: Boolean = true

    private var _binding: DialogFileSelectorBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: FileListAdapter
    private val itemsList: MutableList<FSItem> = mutableListOf()

    private val viewModel: FileSelectorViewModel2 by viewModels { FileSelectorViewModel2.Factory(fileExplorer()) }

    protected abstract fun fileExplorer(): FileExplorer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFirstRun = (null == savedInstanceState)

        _binding = DialogFileSelectorBinding.bind(view)

        listAdapter = FileListAdapter(
            requireContext(),
            R.layout.file_list_item,
            R.id.titleView,
            itemsList
        )

        binding.listView.adapter = listAdapter
//        binding.listView.onItemClickListener = this
//        binding.listView.onItemLongClickListener = this

        viewModel.path.observe(viewLifecycleOwner, ::onPathChanged)
        viewModel.list.observe(viewLifecycleOwner, ::onListChanged)
        viewModel.errorMsg.observe(viewLifecycleOwner, ::onNewError)
        viewModel.isBusy.observe(viewLifecycleOwner, ::onIsBusyChanged)

        binding.confirmSelectionButton.setOnClickListener { onConfirmSelectionClicked() }

        if (isFirstRun)
            viewModel.listInitialDir()
    }


    private fun onPathChanged(s: String?) {
        binding.pathView.text = s
    }

    private fun onListChanged(list: List<FSItem>?) {
        list?.also {
            itemsList.clear()
            itemsList.addAll(list)
        }.also {
            if (0 == list?.size)
                binding.emptyListLabel.visibility = View.VISIBLE
            else
                binding.emptyListLabel.visibility = View.GONE
        }
    }

    private fun onNewError(throwable: Throwable?) {
        binding.errorView.text = ExceptionUtils.getErrorMessage(throwable)
    }

    private fun onIsBusyChanged(b: Boolean?) {
        b?.also {
            binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE
        }
    }


    private fun onConfirmSelectionClicked() {
        setFragmentResult(SELECTED_ITEMS, selectedItemsToBundle())
    }

    private fun selectedItemsToBundle(): Bundle {
        return bundleOf()
    }

    companion object {
        const val SELECTED_ITEMS = "SELECTED_ITEMS"
        const val FS_ITEM = "FS_ITEM"
    }
}