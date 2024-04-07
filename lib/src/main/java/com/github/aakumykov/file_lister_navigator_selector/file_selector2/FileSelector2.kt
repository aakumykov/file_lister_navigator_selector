package com.github.aakumykov.file_lister_navigator_selector.file_selector2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister_navigator_selector.FileListAdapter
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogFileSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.SimpleFSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_dialog.RealSortingModeDialog
import com.github.aakumykov.file_lister_navigator_selector.sorting_mode_dialog.SortingModeDialog
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import com.google.gson.Gson

// TODO: Сделать интерфейс "FileSelector" ?

abstract class FileSelector2<SortingModeType> : DialogFragment(R.layout.dialog_file_selector),
    AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, FragmentResultListener {

    private var _binding: DialogFileSelectorBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: FileListAdapter

    private val viewModel: FileSelectorViewModel2<SortingMode> by viewModels {
        FileSelectorViewModel2.Factory(fileExplorer(), defaultSortingMode())
    }

    private val gson by lazy { Gson() }

    private lateinit var storageAccessHelper: StorageAccessHelper


    protected abstract fun defaultSortingMode(): SortingModeType
    protected abstract fun fileExplorer(): FileExplorer<SortingModeType>
    protected abstract fun dirCreatorDialog(basePath: String): DirCreatorDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogFileSelectorBinding.bind(view)

        storageAccessHelper = StorageAccessHelper.create(this)

        childFragmentManager.setFragmentResultListener(DirCreatorDialog.DIR_NAME, viewLifecycleOwner, ::onDirCreationResult)

        prepareListAdapter()
        prepareButtons()
        prepareFragmentResultListeners()
        prepareViewModel()

        if (null == savedInstanceState)
            viewModel.startWork()
    }

    private fun prepareFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(SortingModeDialog.SORTING_MODE_REQUEST, viewLifecycleOwner, this)
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
        binding.createDirButton.setOnClickListener { onCreateDirClicked() }
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


    private fun onCreateDirClicked() {
        storageAccessHelper.requestWriteAccess {
            dirCreatorDialog(fileExplorer().getCurrentPath())
                .show(childFragmentManager, DirCreatorDialog.TAG)
        }
    }


//    abstract fun sortingModeDialog(): AlertDialog
//    abstract fun sortingModeSelectionListener(): DialogInterface.OnClickListener

    private var sortingModeDialog: SortingModeDialog<SortingMode>? = null

    private fun sortingModeDialog(): SortingModeDialog<SortingMode> {
        if (null == sortingModeDialog)
            sortingModeDialog = RealSortingModeDialog.create(viewModel.currentSortingMode)
        return sortingModeDialog!!
    }

    private fun onSortButtonClicked() {
        sortingModeDialog().show(childFragmentManager, SortingModeDialog.TAG)
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
        val TAG: String = FileSelector2::class.java.simpleName
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

    private fun onDirCreationResult(requestKey: String, resultBundle: Bundle) {
        resultBundle.getString(DirCreatorDialog.DIR_NAME)?.also {
            Toast.makeText(requireContext(), getString(R.string.dir_was_created, it), Toast.LENGTH_SHORT).show()
        }
        reopenCurrentDir()
    }


    private fun reopenCurrentDir() {
//        openDir(fileExplorer().getCurrentDir())
        viewModel.reopenCurrentDir()
    }

    private fun reopenCurrentDir(sortingMode: SortingMode) {
//        openDir(fileExplorer().getCurrentDir(), sortingMode)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            SortingModeDialog.SORTING_MODE_REQUEST -> onSortingModeChanged(result)
            else -> {}
        }
    }

    private fun onSortingModeChanged(result: Bundle) {
        val sortingMode = sortingModeDialog().sortingNameToSortingMode(result.getString(SortingModeDialog.SORTING_MODE_NAME))
        viewModel.changeSortingMode(sortingMode)
    }

}