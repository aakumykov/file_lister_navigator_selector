package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_dialog

import androidx.core.os.bundleOf
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode

class RealSortingModeDialog : SortingModeDialog<SortingMode>() {

    override fun sortingModesMap(): Map<String, String> {
        return mapOf(
            SortingMode.NAME_DIRECT.name to getString(R.string.sorting_mode_name_direct),
            SortingMode.NAME_REVERSE.name to getString(R.string.sorting_mode_name_reverse),

            SortingMode.C_TIME_DIRECT.name to getString(R.string.sorting_mode_c_time_direct),
            SortingMode.C_TIME_REVERSE.name to getString(R.string.sorting_mode_c_time_reverse),

            SortingMode.M_TIME_DIRECT.name to getString(R.string.sorting_mode_m_time_direct),
            SortingMode.M_TIME_REVERSE.name to getString(R.string.sorting_mode_m_time_reverse),

            SortingMode.SIZE_DIRECT.name to getString(R.string.sorting_mode_size_direct),
            SortingMode.SIZE_REVERSE.name to getString(R.string.sorting_mode_size_reverse),
        )
    }

    override fun sortingNameToSortingMode(name: String?): SortingMode {
        return if (null != name) SortingMode.valueOf(name)
        else defaultSortingMode()
    }

    override fun defaultSortingMode(): SortingMode = SortingMode.NAME_DIRECT

    companion object {
        fun create(initialSortingMode: SortingMode): RealSortingModeDialog {
            return RealSortingModeDialog().apply {
                arguments = bundleOf(
                    INITIAL_SORTING_MODE to initialSortingMode
                )
            }
        }
    }
}