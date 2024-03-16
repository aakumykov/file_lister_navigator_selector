package com.github.aakumykov.file_lister_navigator_selector.file_selector

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogFileSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.dir_creator_dialog.DirCreatorDialog
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import kotlin.concurrent.thread

typealias Layout = DialogFileSelectorBinding

abstract class FileSelector : DialogFragment(R.layout.dialog_file_selector),
    AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener
{
    //
    // Разметка
    //
    private var _binding: Layout? = null
    private val binding get() = _binding!!

    //
    // Handler для асинхронных операций
    //
    private val handler: Handler = Handler(Looper.getMainLooper())

    //
    // Адаптер списка и список, который он отображает (можно без него?)
    //
    private lateinit var listAdapter: FileListAdapter
    private val itemList: MutableList<FSItem> = mutableListOf()

    //
    // ViewModel хранит текущее состояние интерфейса
    //
    private val viewModel: FileSelectorViewModel by viewModels()

    //
    // Флаг перваго запуску
    //
    private var firstRun: Boolean = true

    //
    // Колбек подтверждения выбора.
    //
    private var callback: Callback? = null


    //
    // Свойства, получаемые из аргументов фрагмента.
    //
    private val isMultipleSelectionMode: Boolean by lazy { arguments?.getBoolean(IS_MULTIPLE_SELECTION_MODE) ?: false }
    private val startPath: String by lazy { arguments?.getString(START_PATH) ?: defaultStartPath() }
    protected val isDirMode: Boolean by lazy { arguments?.getBoolean(IS_DIR_MODE) ?: false }


    //
    // Компоненты, реализуемые наследниками.
    //
    abstract fun fileExplorer(): FileExplorer
    abstract fun defaultStartPath(): String
    abstract fun dirCreatorDialog(basePath: String): DirCreatorDialog


    //
    // StorageAccessHelper
    //
    private lateinit var storageAccessHelper: StorageAccessHelper


    fun show(fragmentManager: FragmentManager): FileSelector {
        show(fragmentManager, TAG)
        return this
    }


    fun setCallback(callback: Callback): FileSelector {
        this.callback = callback
        return this
    }

    fun unsetCallback() {
        this.callback = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = Layout.bind(view)

        firstRun = (null == savedInstanceState)

        storageAccessHelper = StorageAccessHelper.create(this)

        childFragmentManager.setFragmentResultListener(DirCreatorDialog.DIR_NAME, viewLifecycleOwner, ::onDirCreationResult)

        viewModel.fileList.observe(viewLifecycleOwner, ::onFileListChanged)
        viewModel.selectedList.observe(viewLifecycleOwner, ::onSelectionListChanged)
        viewModel.currentPath.observe(viewLifecycleOwner, ::onCurrentPathChanged)
        viewModel.errorMessage.observe(viewLifecycleOwner, ::onErrorChanged)

        listAdapter = FileListAdapter(requireContext(), R.layout.file_list_item, R.id.titleView, itemList)

        binding.pathView.setText(if (isDirMode) R.string.DIALOG_title_dir_mode else R.string.DIALOG_title_common_mode)

        binding.listView.adapter = listAdapter

        binding.listView.onItemClickListener = this
        binding.listView.onItemLongClickListener = this

        binding.createDirButton.setOnClickListener { onCreateDirClicked() }
        binding.dialogSortButton.setOnClickListener { onSortButtonClicked() }
        binding.dialogCloseButton.setOnClickListener { dismiss() }
        binding.confirmSelectionButton.setOnClickListener { onConfirmSelectionClicked() }
    }

    override fun onStart() {
        super.onStart()
        if (firstRun)
            openDir(DirItem.fromPath(startPath))
    }

    private fun onCreateDirClicked() {
        storageAccessHelper.requestWriteAccess {
            dirCreatorDialog(fileExplorer().getCurrentPath()).show(childFragmentManager, DirCreatorDialog.TAG)
        }
    }

    private fun onSortButtonClicked() {
        Toast.makeText(requireContext(), R.string.not_implemented_yet, Toast.LENGTH_SHORT).show()
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


    private fun onDirCreationResult(requestKey: String, resultBundle: Bundle) {
        resultBundle.getString(DirCreatorDialog.DIR_NAME)?.also {
            Toast.makeText(requireContext(), getString(R.string.dir_was_created, it), Toast.LENGTH_SHORT).show()
        }
        openDir(fileExplorer().getCurrentDir())
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemList[position].also { clickedItem ->
            when {
                clickedItem is DirItem -> openDir(clickedItem)
                isMultipleSelectionMode -> onItemClickedInSingleSelectionMode(clickedItem)
                else -> onItemClickedInMultipleSelectionMode(clickedItem)
            }
        }
    }


    private fun onItemClickedInSingleSelectionMode(clickedItem: FSItem) {
        selectItem(clickedItem)
        onConfirmSelectionClicked()
    }

    private fun onItemClickedInMultipleSelectionMode(clickedItem: FSItem) {
        selectItem(clickedItem)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {

        val longClickedItem = itemList[position]

        // Игнорирую попытку выбора родительского каталога.
        if (longClickedItem is ParentDirItem)
            return true

        if (isMultipleSelectionMode) toggleItemSelection(longClickedItem)
        else selectItem(longClickedItem)

        return true
    }


    private fun toggleItemSelection(fsItem: FSItem) {
        viewModel.toggleItemSelection(fsItem)
    }

    private fun selectItem(fsItem: FSItem) {
        viewModel.setSelectedItem(fsItem)
    }


    private fun openDir(dirItem: DirItem) {

        hideError()
        showProgressBar()

        thread {
            try {
                fileExplorer().changeDir(dirItem)
                val list = fileExplorer().listCurrentPath()

                handler.post {
                    hideProgressBar()
                    viewModel.clearSelectionList()
                    viewModel.setFileList(list)
                    viewModel.setCurrentPath(fileExplorer().getCurrentPath())
                }
            }
            catch (throwable: Throwable) {
                handler.post { viewModel.setError(throwable) }
            }
            finally {
                handler.post { hideProgressBar() }
            }
        }
    }


    private fun onErrorChanged(throwable: Throwable?) {
        throwable?.let {
            showError(throwable)
            Log.e(TAG, ExceptionUtils.getErrorMessage(throwable), throwable)
        }
    }


    private fun onCurrentPathChanged(path: String?) {
        binding.pathView.text = path?.let { path } ?: "?"
    }


    private fun onFileListChanged(list: List<FSItem>?) {
        list?.let {
            hideProgressBar()
            itemList.clear()
            itemList.addAll(it)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    private fun showError(throwable: Throwable) {
        showError(ExceptionUtils.getErrorMessage(throwable))
    }
    private fun showError(text: String) {
        binding.errorView.text = text
        binding.errorView.visibility = View.VISIBLE
    }
    private fun hideError() {
        binding.errorView.text = ""
        binding.errorView.visibility = View.GONE
    }


    interface Callback {
        fun onFilesSelected(selectedItemsList: List<FSItem>)
    }


    companion object {
        val TAG: String = FileSelector::class.java.simpleName

        const val START_PATH = "INITIAL_PATH"
        const val IS_MULTIPLE_SELECTION_MODE = "IS_MULTIPLE_SELECTION_MODE"
        const val IS_DIR_MODE = "IS_DIR_MODE"

        @Deprecated("Здесь ему не место")
        const val AUTH_TOKEN = "AUTH_TOKEN"

        fun find(tag: String, fragmentManager: FragmentManager): FileSelector? {
            return when(val fragment = fragmentManager.findFragmentByTag(tag)) {
                is FileSelector -> fragment
                else -> null
            }
        }
    }
}