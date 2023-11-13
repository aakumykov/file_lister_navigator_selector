package com.github.aakumykov.kotlin_playground

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.kotlin_playground.databinding.FragmentMainBinding
import com.github.aakumykov.kotlin_playground.extensions.showToast
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest
import java.lang.StringBuilder

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var storagePermissionRequest: PermissionsRequester


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storagePermissionRequest = constructPermissionsRequest(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = ::onButtonClicked,
            onNeverAskAgain = { showToast("Нужны разрешение на чтение памяти") },
            onPermissionDenied = { showToast("Вы запретили чтение памяти...") }
        )

        _binding = FragmentMainBinding.bind(view)

        binding.button.setOnClickListener { onButtonClicked() }

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun onButtonClicked() {

        val fileExplorer = mainViewModel.getFileExplorer()
        fileExplorer.goToRootDir()

        fileExplorer.listDir(fileExplorer.getCurrentPath()).let { list ->
            val sb = StringBuilder()
            for (fsItem: FSItem in list) {
                sb.append(fsItem.name)
                sb.append("\n")
            }
            binding.textView.text = sb
        }
    }

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun create(): MainFragment {
            return MainFragment()
        }
    }
}