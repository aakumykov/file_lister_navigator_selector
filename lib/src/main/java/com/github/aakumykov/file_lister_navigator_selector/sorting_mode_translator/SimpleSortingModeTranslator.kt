package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator

import android.content.res.Resources
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode

class SimpleSortingModeTranslator(private val resources: Resources) :
    SortingModeTranslator<SimpleSortingMode> {

    override fun sortingModeNames(): Array<String> {
        return SimpleSortingMode.entries.map {
            when(it) {
                SimpleSortingMode.NAME_DIRECT -> resources.getString(R.string.sorting_mode_name_direct)
                SimpleSortingMode.NAME_REVERSE -> resources.getString(R.string.sorting_mode_name_reverse)

                SimpleSortingMode.SIZE_DIRECT -> resources.getString(R.string.sorting_mode_size_direct)
                SimpleSortingMode.SIZE_REVERSE -> resources.getString(R.string.sorting_mode_size_reverse)

                SimpleSortingMode.C_TIME_DIRECT -> resources.getString(R.string.sorting_mode_c_time_direct)
                SimpleSortingMode.C_TIME_REVERSE -> resources.getString(R.string.sorting_mode_c_time_reverse)

                SimpleSortingMode.M_TIME_DIRECT -> resources.getString(R.string.sorting_mode_m_time_direct)
                SimpleSortingMode.M_TIME_REVERSE -> resources.getString(R.string.sorting_mode_m_time_reverse)
            }
        }.toTypedArray()
    }

    override fun sortingNameToSortingMode(name: String): SimpleSortingMode? {
        return when(name) {
            resources.getString(R.string.sorting_mode_name_direct) -> SimpleSortingMode.NAME_DIRECT
            resources.getString (R.string.sorting_mode_name_reverse) -> SimpleSortingMode.NAME_REVERSE

            resources.getString (R.string.sorting_mode_size_direct) -> SimpleSortingMode.SIZE_DIRECT
            resources.getString (R.string.sorting_mode_size_reverse) -> SimpleSortingMode.SIZE_REVERSE

            resources.getString (R.string.sorting_mode_c_time_direct) -> SimpleSortingMode.C_TIME_DIRECT
            resources.getString (R.string.sorting_mode_c_time_reverse) -> SimpleSortingMode.C_TIME_REVERSE

            resources.getString (R.string.sorting_mode_m_time_direct) -> SimpleSortingMode.M_TIME_DIRECT
            resources.getString (R.string.sorting_mode_m_time_reverse) -> SimpleSortingMode.M_TIME_REVERSE

            else -> null
        }
    }

    override fun sortingModeToPosition(mode: SimpleSortingMode): Int {
        return SimpleSortingMode.entries.indexOf(mode)
    }

    override fun positionToSortingMode(position: Int): SimpleSortingMode {
        return SimpleSortingMode.entries[position]
    }
}