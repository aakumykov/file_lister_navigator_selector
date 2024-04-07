package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_dialog

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.databinding.DialogSortingModeBinding
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode

abstract class SortingModeDialog<SortingModeType> : DialogFragment(R.layout.dialog_sorting_mode) {

    private var _binding: DialogSortingModeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapter
    private lateinit var sortingModesMap: Map<String,String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogSortingModeBinding.bind(view)

        sortingModesMap = sortingModesMap()

        listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sortingModesMap.values.toList())

        binding.listView.adapter = listAdapter

        binding.listView.setOnItemClickListener { _, _, position, id ->
            val sortingModeName = sortingModesMap.keys.toList()[position]
            setFragmentResult(SORTING_MODE_REQUEST, bundleOf(
                SORTING_MODE_NAME to sortingModeName
            ))
            dismiss()
        }
    }

    abstract fun sortingModesMap(): Map<String, String>
    abstract fun sortingNameToSortingMode(name: String?): SortingModeType
    abstract fun defaultSortingMode(): SortingModeType

    companion object {
        val TAG: String = SortingModeDialog::class.java.simpleName
        const val SORTING_MODE_REQUEST = "SORTING_MODE_REQUEST"
        const val SORTING_MODE_NAME = "SORTING_MODE_NAME"
        const val INITIAL_SORTING_MODE= "INITIAL_SORTING_MODE"
    }
}