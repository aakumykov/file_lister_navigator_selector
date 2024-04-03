package com.github.aakumykov.file_lister_navigator_selector

import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.DummySortingComparator
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.MTimeSortingComparator
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.NameSortingComparator
import com.github.aakumykov.file_lister_navigator_selector.sorting_comparator.SizeSortingComparator

class ComparatorFactory {

    fun createComparator(
        sortingMode: SortingMode,
        reverseOrder: Boolean,
        foldersFirst: Boolean
    ): Comparator<in FSItem> {
        return when(sortingMode) {
            SortingMode.NAME -> NameSortingComparator(reverseOrder, foldersFirst)
            SortingMode.SIZE -> SizeSortingComparator(reverseOrder, foldersFirst)
            SortingMode.M_TIME -> MTimeSortingComparator(reverseOrder, foldersFirst)
            else -> DummySortingComparator()
        }
    }
}