package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.kotlin_playground.databinding.FragmentYandexBinding
import com.github.aakumykov.kotlin_playground.extensions.restoreString
import com.github.aakumykov.kotlin_playground.extensions.showToast
import com.github.aakumykov.kotlin_playground.extensions.storeString
import com.github.aakumykov.yandex_disk_file_explorer.YandexDiskFileExplorer
import com.github.aakumykov.yandex_disk_file_lister.YandexDiskFileLister
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdkContract
import kotlin.concurrent.thread

class YandexFragment : Fragment(R.layout.fragment_yandex) {

    private var _binding: FragmentYandexBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FileExplorerViewModel by viewModels()

    private lateinit var yandexAuthLauncher: ActivityResultLauncher<YandexAuthLoginOptions>
    private var yandexAuthToken: String? = null

    private val itemsList = mutableListOf<FSItem>()
    private lateinit var listAdapter: ListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Этот метод должен выбываться первым.
        restoreYandexAuthToken()

        _binding = FragmentYandexBinding.bind(view)

        if (null == savedInstanceState)
            prepareFileExplorer()

        prepareViewModel()
        prepareButtons()
        prepareListAdapter()
        prepareYandexAuth()

        displayYandexAuthStatus()
    }

    private fun prepareViewModel() {
        viewModel.currentPath.observe(viewLifecycleOwner, ::onCurrentPathChanged)
        viewModel.currentList.observe(viewLifecycleOwner, ::onCurrentListChanged)
    }

    private fun onCurrentListChanged(list: List<FSItem>?) {
        list?.let { displayList(it) }
    }

    private fun onCurrentPathChanged(path: String?) {
        path?.let { uiRun {
            binding.listButton.text = path
        } }
    }

    private fun prepareFileExplorer() {
        val fileLister = YandexDiskFileLister(yandexAuthToken!!)
        val fileExplorer = YandexDiskFileExplorer(fileLister, listCache = viewModel, pathCache = viewModel)
        viewModel.setFileExplorer(fileExplorer)
    }

    private fun prepareListAdapter() {
        listAdapter = ListAdapter(requireContext(), R.layout.list_item, itemsList)
        binding.listView.adapter = listAdapter
    }

    private fun prepareButtons() {
        binding.yandexButton.setOnClickListener { onYandexButtonClicked() }
        binding.listButton.setOnClickListener { onListButtonClicked() }
    }

    private fun onListButtonClicked() {
        if (null == yandexAuthToken) {
            showToast("Авторизуйтесь в Яндекс")
            return
        }

        showProgressBar()
        hideError()

        thread {
            try {
                viewModel.fileExplorer.listCurrentPath()
            } catch (t: Throwable) {
                uiRun { showError(t) }
            } finally {
                uiRun { hideProgressBar() }
            }
        }
    }

    private fun uiRun(function: () -> Unit) {
        binding.root.post(function)
    }

    private fun showError(t: Throwable) {
        binding.errorView.text = ExceptionUtils.getErrorMessage(t)
        binding.errorView.visibility = View.VISIBLE
    }
    private fun hideError() {
        binding.errorView.text = ""
        binding.errorView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun displayList(list: List<FSItem>) {
        uiRun {
            itemsList.clear()
            itemsList.addAll(list)
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun onYandexButtonClicked() {
        if (null == yandexAuthToken)
            yandexAuthLauncher.launch(YandexAuthLoginOptions())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun prepareYandexAuth() {

        val yandexAuthOptions by lazy {
            YandexAuthOptions(requireContext(), true)
        }

        val yandexAuthSdkContract by lazy {
            YandexAuthSdkContract(yandexAuthOptions)
        }

        yandexAuthLauncher = registerForActivityResult(yandexAuthSdkContract) { result ->
            yandexAuthToken = result.getOrNull()?.value
            storeYandexAuthToken()
            displayYandexAuthStatus()
        }
    }

    private fun storeYandexAuthToken() {
        storeString(YANDEX_AUTH_TOKEN, yandexAuthToken)
    }

    private fun restoreYandexAuthToken() {
        yandexAuthToken = restoreString(YANDEX_AUTH_TOKEN)
    }

    private fun displayYandexAuthStatus() {
        if (null != yandexAuthToken) {
            with (binding.yandexButton) {
                setIconResource(R.drawable.ic_logged_in)
                text = getString(R.string.logout_from_yandex)
            }
        } else {
            with(binding.yandexButton) {
                setIconResource(R.drawable.ic_logged_out)
                text = getString(R.string.login_to_yandex)
            }
        }
    }


    companion object {
        const val YANDEX_AUTH_TOKEN = "YANDEX_AUTH_TOKEN"
        fun create(): YandexFragment {
            return YandexFragment()
        }
    }
}