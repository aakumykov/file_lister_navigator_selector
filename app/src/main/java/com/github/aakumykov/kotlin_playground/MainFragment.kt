package com.github.aakumykov.kotlin_playground

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.kotlin_playground.databinding.FragmentMainBinding
import com.github.aakumykov.kotlin_playground.extensions.showToast
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest
import java.io.IOException
import java.lang.Exception

class MainFragment : Fragment(R.layout.fragment_main), AdapterView.OnItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var storagePermissionRequest: PermissionsRequester

    private val itemsList = mutableListOf<FSItem>()
    private lateinit var listAdapter: ListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storagePermissionRequest = constructPermissionsRequest(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = ::listDir,
            onNeverAskAgain = { showToast("Нужны разрешение на чтение памяти") },
            onPermissionDenied = { showToast("Вы запретили чтение памяти...") }
        )

        _binding = FragmentMainBinding.bind(view)

        binding.button.setOnClickListener { listDirWithPermissions() }

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        listAdapter = ListAdapter(requireActivity(), R.layout.list_item, itemsList)
        binding.listView.adapter = listAdapter

        binding.listView.setOnItemClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        listDirWithPermissions()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun listDirWithPermissions() {
        storagePermissionRequest.launch()
    }


    private fun listDir() {

        hideError()
        showProgressBar()

        val fileExplorer = mainViewModel.getFileExplorer()
        fileExplorer.goToRootDir()

        try {
            hideProgressBar()

            itemsList.clear()
            itemsList.addAll(fileExplorer.listDir(fileExplorer.getCurrentPath()))
            listAdapter.notifyDataSetChanged()

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


    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun create(): MainFragment {
            return MainFragment()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val fsItem = itemsList[position]

    }
}