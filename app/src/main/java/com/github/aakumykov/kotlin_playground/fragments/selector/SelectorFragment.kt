package com.github.aakumykov.kotlin_playground.fragments.selector

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_selector.FileSelector
import com.github.aakumykov.kotlin_playground.R
import com.github.aakumykov.kotlin_playground.databinding.FragmentSelectorBinding
import com.github.aakumykov.kotlin_playground.extensions.showToast
import com.github.aakumykov.local_file_selector.LocalFileSelector
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class SelectorFragment : Fragment(R.layout.fragment_selector), FileSelector.Callback {

    private var _binding: FragmentSelectorBinding? = null
    private val binding: FragmentSelectorBinding get() = _binding!!
    private lateinit var selectFilePermissionRequest: PermissionsRequester

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSelectorBinding.bind(view)

        binding.selectButton.setOnClickListener { selectFilePermissionRequest.launch() }

        selectFilePermissionRequest = constructPermissionsRequest(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = ::showFileSelector,
            onNeverAskAgain = { showToast("Нужны разрешение на чтение памяти") },
            onPermissionDenied = { showToast("Вы запретили чтение памяти...") }
        )
    }


    private fun showFileSelector() {
        val fileSelector = LocalFileSelector.create(isMultipleSelectionMode = true).show(childFragmentManager)
        fileSelector.setCallback(this)
    }

    override fun onDestroyView() {
        FileSelector.find(childFragmentManager)?.unsetCallback()
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