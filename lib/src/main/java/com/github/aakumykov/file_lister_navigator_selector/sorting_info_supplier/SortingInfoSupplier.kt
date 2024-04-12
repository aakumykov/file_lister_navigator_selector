package com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

interface SortingInfoSupplier<SortingModeType> {
    fun getSortingInfo(fsItem: FSItem, sortingMode: SortingModeType): String
}