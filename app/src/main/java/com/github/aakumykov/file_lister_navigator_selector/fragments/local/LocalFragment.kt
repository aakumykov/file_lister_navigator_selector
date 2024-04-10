package com.github.aakumykov.file_lister_navigator_selector.fragments.local

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.common.ListAdapter
import com.github.aakumykov.file_lister_navigator_selector.databinding.FragmentLocalBinding
import com.github.aakumykov.file_lister_navigator_selector.extensions.showToast
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.local_file_lister.LocalFileLister
import com.github.aakumykov.file_lister_navigator_selector.recursive_dir_reader.RecursiveDirReader
import com.github.aakumykov.file_lister_navigator_selector.utils.AndroidVersionHelper
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import java.io.IOException

class LocalFragment : Fragment(R.layout.fragment_local), AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {

    private var _binding: FragmentLocalBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLocalViewModel: LocalViewModel
    private lateinit var storageAccessHelper: StorageAccessHelper

    private val itemsList = mutableListOf<FSItem>()
    private lateinit var listAdapter: ListAdapter

    private val fileExplorer: FileExplorer<SimpleSortingMode> get() = mLocalViewModel.getFileExplorer()

    private var isFirstRun: Boolean = true

    private var currentSortingMode: SimpleSortingMode = SimpleSortingMode.NAME


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFirstRun = null == savedInstanceState

        storageAccessHelper = StorageAccessHelper.create(this)

        _binding = FragmentLocalBinding.bind(view)

        binding.manageAllFilesButton.setOnClickListener { onManageAllFilesButtonClicked() }

        binding.button.setOnClickListener {
            storageAccessHelper.requestReadAccess {
                listCurrentDir()
            }
        }

        mLocalViewModel = ViewModelProvider(requireActivity()).get(LocalViewModel::class.java)

        mLocalViewModel.currentList.observe(viewLifecycleOwner, ::onListChanged)
        mLocalViewModel.currentPath.observe(viewLifecycleOwner, ::onPathChanged)

        listAdapter = ListAdapter(requireActivity(), R.layout.list_item, itemsList)
        binding.listView.adapter = listAdapter

        binding.listView.setOnItemClickListener(this)
        binding.listView.setOnItemLongClickListener(this)

        binding.button.text = fileExplorer.getCurrentPath()
    }

    private fun onManageAllFilesButtonClicked() {
        if (AndroidVersionHelper.is_android_R_or_later())
            showManageAllFilesDialog()
        else
            showToast("Неприменимо к этой версии Android")
    }

    // FIXME: package name
    @RequiresApi(Build.VERSION_CODES.R)
    private fun showManageAllFilesDialog() {
        val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:${requireActivity().packageName}")
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        mLocalViewModel.startWork()
    }


    private fun onListChanged(fsItems: List<FSItem>?) {
        fsItems?.let { list ->
            itemsList.clear()
            itemsList.addAll(list)
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun onPathChanged(path: String?) {
        path?.let { binding.button.text = it }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun listCurrentDir() {

        hideError()
        showProgressBar()

        try {
            hideProgressBar()
            fileExplorer.listCurrentPath()
            binding.button.text = getString(R.string.list_of_files_in, fileExplorer.getCurrentPath())
        }
        catch (e: IOException) {
            showError(e)
            hideProgressBar()
        }
    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    private fun showError(e: Exception) {
        binding.errorView.text = ExceptionUtils.getErrorMessage(e)
        binding.errorView.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.errorView.text = ""
        binding.errorView.visibility = View.GONE
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val fsItem = itemsList[position]
        if (fsItem is DirItem)
            fileExplorer.changeDir(fsItem).also {
                listCurrentDir()
            }
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {

        val fsItem = itemsList[position]

        val recursiveDirReader = RecursiveDirReader(LocalFileLister(""))

        val recursiveList = recursiveDirReader.getRecursiveList(fsItem.absolutePath, currentSortingMode)

        AlertDialog.Builder(requireContext())
            .setTitle("Рекурсивный список содержимого")
            .setMessage(recursiveList.joinToString(
                separator = "\n\n",
                transform = { fileListItem -> fileListItem.absolutePath })
            )
            .setNeutralButton("Закрыть") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        return true
    }

    companion object {
        val TAG: String = LocalFragment::class.java.simpleName
        fun create(): LocalFragment {
            return LocalFragment()
        }
    }
}