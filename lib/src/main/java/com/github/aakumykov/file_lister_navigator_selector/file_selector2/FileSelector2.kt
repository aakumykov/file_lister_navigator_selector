package com.github.aakumykov.file_lister_navigator_selector.file_selector2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister_navigator_selector.FileListAdapter
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogFileSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import com.google.gson.Gson

abstract class FileSelector2 : DialogFragment(R.layout.dialog_file_selector),
    AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private var _binding: DialogFileSelectorBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: FileListAdapter
    private val viewModel: FileSelectorViewModel2 by viewModels { FileSelectorViewModel2.Factory(fileExplorer()) }

    private val gson by lazy { Gson() }


    protected abstract fun fileExplorer(): FileExplorer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogFileSelectorBinding.bind(view)

        prepareListAdapter()
        prepareButtons()
        prepareViewModel()

        if (null == savedInstanceState)
            viewModel.startWork()
    }

    private fun prepareViewModel() {
        viewModel.path.observe(viewLifecycleOwner, ::onPathChanged)
        viewModel.list.observe(viewLifecycleOwner, ::onListChanged)
        viewModel.selectedList.observe(viewLifecycleOwner, ::onSelectedListChanged)
        viewModel.errorMsg.observe(viewLifecycleOwner, ::onNewError)
        viewModel.isBusy.observe(viewLifecycleOwner, ::onIsBusyChanged)
    }

    private fun prepareButtons() {
        binding.confirmSelectionButton.setOnClickListener { onConfirmSelectionClicked() }
        binding.dialogCloseButton.setOnClickListener { dismiss() }
        binding.sortButton.setOnClickListener { onSortButtonClicked() }
    }

    private fun prepareListAdapter() {

        listAdapter = FileListAdapter(
            requireContext(),
            R.layout.file_list_item,
            R.id.titleView)

        binding.listView.adapter = listAdapter

        binding.listView.onItemClickListener = this
        binding.listView.onItemLongClickListener = this
    }


    private fun onPathChanged(s: String?) {
        binding.pathView.text = s
    }

    private fun onListChanged(list: List<FSItem>?) {
        list?.also {
            listAdapter.setList(it)
        }.also {
            if (0 == list?.size)
                binding.emptyListLabel.visibility = View.VISIBLE
            else
                binding.emptyListLabel.visibility = View.GONE
        }
    }

    private fun onSelectedListChanged(selectedItemsList: List<FSItem>?) {
        selectedItemsList?.also {
            listAdapter.updateSelections(selectedItemsList)
            binding.confirmSelectionButton.isEnabled = (selectedItemsList.size > 0)
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


    private fun onSortButtonClicked() {

    }


    private fun onConfirmSelectionClicked() {
        setFragmentResult(ITEMS_SELECTION, selectedItemsToBundle())
        dismiss()
    }

    private fun selectedItemsToBundle(): Bundle {
        val listOfJSON = viewModel.selectedList.value?.map {
            gson.toJson(it, SimpleFSItem::class.java)
        }
        return bundleOf(SELECTED_ITEMS_LIST to listOfJSON)
    }

    companion object {
        const val ITEMS_SELECTION = "ITEMS_SELECTION"
        const val SELECTED_ITEMS_LIST = "FS_ITEM"
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.onItemClick(position)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        viewModel.onItemLongClick(position)
        return true
    }
}