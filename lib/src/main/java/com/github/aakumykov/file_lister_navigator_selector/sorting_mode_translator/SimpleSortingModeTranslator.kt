package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator

import android.content.res.Resources
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode

class SimpleSortingModeTranslator(private val resources: Resources) :
    SortingModeTranslator<SortingMode> {

    override fun sortingModeNames(): Array<String> {
        return SortingMode.entries.map {
            when(it) {
                SortingMode.NAME_DIRECT -> resources.getString(R.string.sorting_mode_name_direct)
                SortingMode.NAME_REVERSE -> resources.getString(R.string.sorting_mode_name_reverse)

                SortingMode.SIZE_DIRECT -> resources.getString(R.string.sorting_mode_size_direct)
                SortingMode.SIZE_REVERSE -> resources.getString(R.string.sorting_mode_size_reverse)

                SortingMode.C_TIME_DIRECT -> resources.getString(R.string.sorting_mode_c_time_direct)
                SortingMode.C_TIME_REVERSE -> resources.getString(R.string.sorting_mode_c_time_reverse)

                SortingMode.M_TIME_DIRECT -> resources.getString(R.string.sorting_mode_m_time_direct)
                SortingMode.M_TIME_REVERSE -> resources.getString(R.string.sorting_mode_m_time_reverse)
            }
        }.toTypedArray()
    }

    override fun sortingNameToSortingMode(name: String): SortingMode? {
        return when(name) {
            resources.getString(R.string.sorting_mode_name_direct) -> SortingMode.NAME_DIRECT
            resources.getString (R.string.sorting_mode_name_reverse) -> SortingMode.NAME_REVERSE

            resources.getString (R.string.sorting_mode_size_direct) -> SortingMode.SIZE_DIRECT
            resources.getString (R.string.sorting_mode_size_reverse) -> SortingMode.SIZE_REVERSE

            resources.getString (R.string.sorting_mode_c_time_direct) -> SortingMode.C_TIME_DIRECT
            resources.getString (R.string.sorting_mode_c_time_reverse) -> SortingMode.C_TIME_REVERSE

            resources.getString (R.string.sorting_mode_m_time_direct) -> SortingMode.M_TIME_DIRECT
            resources.getString (R.string.sorting_mode_m_time_reverse) -> SortingMode.M_TIME_REVERSE

            else -> null
        }
    }

    override fun sortingModeToPosition(mode: SortingMode): Int {
        return SortingMode.entries.indexOf(mode)
    }

    override fun positionToSortingMode(position: Int): SortingMode {
        return SortingMode.entries[position]
    }
}