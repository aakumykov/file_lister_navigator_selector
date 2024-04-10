package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator

import android.content.res.Resources
import androidx.annotation.StringRes
import com.github.aakumykov.file_lister_navigator_selector.R
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode

class SimpleSortingModeTranslator(
    private val resources: Resources,
    private val directOrderArrow: String = "⬇\uFE0F",
    private val reverseOrderArrow: String = "⬆\uFE0F",
) :
    SortingModeTranslator<SimpleSortingMode> {

    override fun sortingModeNames(reverseOrder: Boolean,
                                  selectedItem: SimpleSortingMode): Array<String>
    {
        return SimpleSortingMode.entries.map {
            when(it) {
                SimpleSortingMode.NAME -> qwerty(SimpleSortingMode.NAME, R.string.sorting_mode_name, reverseOrder, selectedItem)
                SimpleSortingMode.SIZE -> qwerty(SimpleSortingMode.SIZE, R.string.sorting_mode_size, reverseOrder, selectedItem)
                SimpleSortingMode.C_TIME -> qwerty(SimpleSortingMode.C_TIME, R.string.sorting_mode_c_time, reverseOrder, selectedItem)
                SimpleSortingMode.M_TIME -> qwerty(SimpleSortingMode.M_TIME, R.string.sorting_mode_m_time, reverseOrder, selectedItem)
            }
        }.toTypedArray()
    }

    private fun qwerty(sortingMode: SimpleSortingMode, @StringRes sortingModeName: Int,
                       reverseOrder: Boolean, selectedItem: SimpleSortingMode): String
    {
        val name = resources.getString(sortingModeName)
        if (sortingMode)
    }

    override fun sortingNameToSortingMode(name: String): SimpleSortingMode? {
        return when(name) {
            resources.getString(R.string.sorting_mode_name) -> SimpleSortingMode.NAME
            resources.getString (R.string.sorting_mode_size) -> SimpleSortingMode.SIZE
            resources.getString (R.string.sorting_mode_c_time) -> SimpleSortingMode.C_TIME
            resources.getString (R.string.sorting_mode_m_time) -> SimpleSortingMode.M_TIME
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