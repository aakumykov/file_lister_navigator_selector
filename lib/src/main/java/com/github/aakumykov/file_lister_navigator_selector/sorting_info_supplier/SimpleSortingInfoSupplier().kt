package com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier

import com.github.aakumykov.file_lister_navigator_selector.file_lister.SimpleSortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.utils.DateFormatter

class SimpleSortingInfoSupplier : SortingInfoSupplier<SimpleSortingMode> {

    override fun getSortingInfo(fsItem: FSItem, sortingMode: SimpleSortingMode): String {
        return when(sortingMode) {
            SimpleSortingMode.NAME -> ""
            SimpleSortingMode.SIZE -> fsItem.size.toString()
            SimpleSortingMode.C_TIME -> DateFormatter.humanReadableDate(fsItem.mTime)
            SimpleSortingMode.M_TIME -> DateFormatter.humanReadableDate(fsItem.mTime)
        }
    }
}