package com.github.aakumykov.file_lister_navigator_selector.fragments.selector

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.file_lister_navigator_selector.LocalFileSelector2.LocalFileSelector2
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.FragmentSelectorBinding
import com.github.aakumykov.file_lister_navigator_selector.extensions.showToast
import com.github.aakumykov.file_lister_navigator_selector.file_selector.FileSelector
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.local_file_selector.LocalFileSelector
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

        /*LocalFileSelector.create(
                callback = this,
                isMultipleSelectionMode = true
            )
            .apply {
                setCallback(this@SelectorFragment)
            }
            .show(childFragmentManager)*/

//                YandexDiskFileSelector.create(yandexAuthToken!!)

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