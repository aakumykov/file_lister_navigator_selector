package com.github.aakumykov.file_lister_navigator_selector.file_selector

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogFileSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.dir_creation_dialog.create_dir_dialog.CreateDirDialog
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.storage_access_helper.storage_access_helper.StorageAccessHelper
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

typealias Layout = DialogFileSelectorBinding

abstract class FileSelectorDialog : DialogFragment(R.layout.dialog_file_selector),
    AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener,
    StorageAccessHelper.ResultCallback
{
    private var _binding: Layout? = null
    private val binding get() = _binding!!

    private val viewModel: FileSelectorViewModel by viewModels()

    private var firstRun: Boolean = true

    private val itemList: MutableList<FSItem> = mutableListOf()
    private lateinit var listAdapter: FileListAdapter

    private var callback: Callback? = null

    private val isMultipleSelectionMode: Boolean by lazy {
        arguments?.getBoolean(IS_MULTIPLE_SELECTION_MODE) ?: false
    }

    protected val isDirMode: Boolean by lazy {
        arguments?.getBoolean(IS_DIR_MODE) ?: false
    }

    private lateinit var storageAccessHelper: StorageAccessHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageAccessHelper = StorageAccessHelper.create(this, this)

        _binding = Layout.bind(view)
        binding.dialogHeaderInclude.titleView.setText(R.string.select_file_title)

        firstRun = (null == savedInstanceState)

        viewModel.fileList.observe(viewLifecycleOwner, ::onFileListChanged)
        viewModel.selectedList.observe(viewLifecycleOwner, ::onSelectionListChanged)
        viewModel.currentPath.observe(viewLifecycleOwner, ::onCurrentPathChanged)
        viewModel.errorMessage.observe(viewLifecycleOwner, ::onErrorChanged)

        listAdapter = FileListAdapter(requireContext(), R.layout.file_list_item, R.id.titleView, itemList)
        binding.listView.adapter = listAdapter
        binding.listView.onItemClickListener = this
        binding.listView.onItemLongClickListener = this

        binding.refreshButton.setOnClickListener { refreshList() }
        binding.swipeRefreshLayout.setOnRefreshListener { refreshList() }

        binding.createDirButton.setOnClickListener { onCreateDirClicked() }
        binding.dialogHeaderInclude.closeButton.setOnClickListener{ dismiss() }
        binding.confirmSelectionButton.setOnClickListener { onConfirmSelectionClicked() }

        subscribeToDirCreationEvent()
    }

    override fun onStorageAccessResult(grantedMode: StorageAccessHelper.StorageAccessMode) {
        when(grantedMode) {
            StorageAccessHelper.StorageAccessMode.WRITING_YES -> showCreateDirDialog()
            StorageAccessHelper.StorageAccessMode.FULL_YES -> showCreateDirDialog()
            else -> Toast.makeText(requireContext(), R.string.no_write_access, Toast.LENGTH_SHORT).show()
        }
    }


    private fun showCreateDirDialog() {
        createDirDialog().show(childFragmentManager, CreateDirDialog.TAG)
        subscribeToDirCreationEvent()
    }

    private fun subscribeToDirCreationEvent() {
        CreateDirDialog.find(childFragmentManager)?.dirCreationEventLiveData?.observe(viewLifecycleOwner, ::onDirCreated)
    }


    private fun onDirCreated(dirName: String) {
        refreshList()
    }


    abstract fun fileExplorer(): FileExplorer

    abstract fun defaultStartPath(): String


    fun show(fragmentManager: FragmentManager): FileSelectorDialog {
        show(fragmentManager, TAG)
        return this
    }


    fun setCallback(callback: Callback): FileSelectorDialog {
        this.callback = callback
        return this
    }

    fun unsetCallback() {
        this.callback = null
    }




    private fun refreshList() {
        lifecycleScope.launch {

            hideError()
            showRefreshIndicator()

            val list: MutableList<FSItem> = mutableListOf()

            withContext(Dispatchers.IO) {
                list.addAll(openAndListDir(dirItemFromPath(fileExplorer().getCurrentPath())))
            }

            viewModel.clearSelectionList()
            viewModel.setFileList(list)
            viewModel.setCurrentPath(fileExplorer().getCurrentPath())

            hideRefreshIndicator()
        }
    }

    private fun showRefreshIndicator() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun hideRefreshIndicator() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onCreateDirClicked() {
        storageAccessHelper.requestWritingAccess()
    }

    abstract fun createDirDialog(): CreateDirDialog

    override fun onStart() {
        super.onStart()

        if (firstRun)
            refreshList()
    }


    private fun dirItemFromPath(path: String): DirItem {
        return DirItem(
            name = path,
            absolutePath = path,
            parentPath = "",
            mTime = Date().time,
        )
    }

    private fun onConfirmSelectionClicked() {
        callback?.onFilesSelected(viewModel.getSelectedList())
        dismiss()
    }

    private fun onSelectionListChanged(selectionList: List<FSItem>?) {
        selectionList?.let { list ->
            listAdapter.updateSelections(list)
            binding.confirmSelectionButton.isEnabled = list.isNotEmpty()
        }
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val clickedItem = itemList[position]

        if (!isMultipleSelectionMode && !clickedItem.isDir) {
            selectItem(clickedItem)
            onConfirmSelectionClicked()
            return
        }

        openDir(clickedItem)
    }

    // FIXME: неверная логика
    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val currentItem = itemList[position]

        // Игнорирую попытку выбора ссылки на родительский каталог.
        if (currentItem is ParentDirItem)
            return true

        if (isMultipleSelectionMode) toggleItemSelection(currentItem)
        else selectItem(currentItem)

        return true
    }


    private fun toggleItemSelection(fsItem: FSItem) {
        viewModel.toggleItemSelection(fsItem)
    }

    private fun selectItem(fsItem: FSItem) {
        viewModel.setSelectedItem(fsItem)
    }


    private fun openDir(fsItem: FSItem) {
        if (!fsItem.isDir)
            return
        fileExplorer().changeDir(fsItem)
        refreshList()
    }

    private fun openAndListDir(dirItem: DirItem): List<FSItem> {
        fileExplorer().changeDir(dirItem)
        return fileExplorer().listCurrentPath()
    }

    private fun onErrorChanged(throwable: Throwable?) {
        throwable?.let {
            showError(throwable)
            Log.e(TAG, ExceptionUtils.getErrorMessage(throwable), throwable)
        }
    }


    private fun onCurrentPathChanged(path: String?) {
        binding.dialogHeaderInclude.titleView.text = path?.let { path } ?: "?"
    }


    private fun onFileListChanged(list: List<FSItem>?) {
        list?.let {
            hideRefreshIndicator()
            itemList.clear()
            itemList.addAll(it)
            listAdapter.notifyDataSetChanged()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun showError(throwable: Throwable) {
        showError(ExceptionUtils.getErrorMessage(throwable))
    }
    private fun showError(text: String) {
        binding.dialogFooterInclude.errorView.text = text
        binding.dialogFooterInclude.errorView.visibility = View.VISIBLE
    }
    private fun hideError() {
        binding.dialogFooterInclude.errorView.text = ""
        binding.dialogFooterInclude.errorView.visibility = View.GONE
    }


    interface Callback {
        fun onFilesSelected(selectedItemsList: List<FSItem>)
    }


    companion object {
        val TAG: String = FileSelectorDialog::class.java.simpleName

        const val START_PATH = "INITIAL_PATH"
        const val IS_MULTIPLE_SELECTION_MODE = "IS_MULTIPLE_SELECTION_MODE"
        const val IS_DIR_MODE = "IS_DIR_MODE"
        const val AUTH_TOKEN = "AUTH_TOKEN"

        fun find(tag: String, fragmentManager: FragmentManager): FileSelectorDialog? {
            return when(val fragment = fragmentManager.findFragmentByTag(tag)) {
                is FileSelectorDialog -> fragment
                else -> null
            }
        }
    }
}