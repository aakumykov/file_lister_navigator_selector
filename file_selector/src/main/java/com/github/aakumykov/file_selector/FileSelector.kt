package com.github.aakumykov.file_selector

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_explorer.FileExplorer
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.SimpleFSItem
import com.github.aakumykov.file_selector.databinding.DialogFileSelectorBinding
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import kotlin.concurrent.thread

typealias Layout = DialogFileSelectorBinding

// FIXME: одновременное ручное и автоматизированное убирание коллбека неправильно.
abstract class FileSelector
    : DialogFragment(),
//    DefaultLifecycleObserver,
    AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener
{
    private var _binding: Layout? = null
    private val binding get() = _binding!!

    private val viewModel: FileSelectorViewModel by viewModels()

    private var firstRun: Boolean = true
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    private val itemList: MutableList<FSItem> = mutableListOf()
    private lateinit var listAdapter: FileListAdapter

    private var callback: Callback? = null

    private val isMultipleSelectionMode: Boolean by lazy {
        arguments?.getBoolean(IS_MULTIPLE_SELECTION_MODE) ?: false
    }

    private val startPath: String by lazy {
        arguments?.getString(START_PATH) ?: defaultStartPath()
    }

    //
    // Используется реализацией абстрактного метода fileLister() для создания объекта FileLister.
    //
    protected val isDirMode: Boolean by lazy {
        arguments?.getBoolean(IS_DIR_MODE) ?: false
    }


    abstract fun fileExplorer(): FileExplorer

    abstract fun defaultStartPath(): String


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


    /*override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        callback?.let { setCallback(it) }
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        this.callback = null
    }*/


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = Layout.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstRun = (null == savedInstanceState)

        viewModel.fileList.observe(viewLifecycleOwner, ::onFileListChanged)
        viewModel.selectedList.observe(viewLifecycleOwner, ::onSelectionListChanged)
        viewModel.currentPath.observe(viewLifecycleOwner, ::onCurrentPathChanged)
        viewModel.errorMessage.observe(viewLifecycleOwner, ::onErrorChanged)

        listAdapter = FileListAdapter(requireContext(), R.layout.file_list_item, R.id.titleView, itemList)

        binding.listView.adapter = listAdapter

        binding.listView.onItemClickListener = this
        binding.listView.onItemLongClickListener = this

        binding.dialogCloseButton.setOnClickListener { dismiss() }
        binding.confirmSelectionButton.setOnClickListener { onConfirmSelectionClicked() }
    }

    override fun onStart() {
        super.onStart()

        if (firstRun)
            openDir(SimpleFSItem(startPath, startPath, true))
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
        val fsItem = itemList[position]
        openDir(fsItem)
    }

    // FIXME: неверная логика
    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        if (isMultipleSelectionMode)
            viewModel.toggleItemSelection(itemList[position])
        else
            viewModel.setSelectedItem(itemList[position])
        return true
    }


    private fun openDir(fsItem: FSItem) {

        if (!fsItem.isDir)
            return

        hideError()
        showProgressBar()

        thread {
            try {
                fileExplorer().changeDir(fsItem)
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
        const val AUTH_TOKEN = "AUTH_TOKEN"

        fun find(fragmentManager: FragmentManager): FileSelector? {
            return when(val fragment = fragmentManager.findFragmentByTag(TAG)) {
                is FileSelector -> fragment
                else -> null
            }
        }
    }
}