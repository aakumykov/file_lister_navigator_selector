package com.github.aakumykov.file_lister_navigator_selector.sorting_comparator

import com.github.aakumykov.extensible_sorting_comparator.ExtensibleSortingComparator
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

abstract class FSItemSortingComparator(reverseOrder: Boolean, foldersFirst: Boolean)
    : ExtensibleSortingComparator<FSItem>(reverseOrder, foldersFirst)
{
    override fun isPriorityItem(item: FSItem): Boolean = item.isDir

    companion object {
        fun <T> create(sortingMode: T): FSItemSortingComparator {
            return when(sortingMode) {
                SortingMode.NAME_DIRECT -> NameSortingComparator(true, foldersFirst = true)
                SortingMode.NAME_REVERSE -> NameSortingComparator(false, foldersFirst = true)

                SortingMode.SIZE_DIRECT -> SizeSortingComparator(true, foldersFirst = true)
                SortingMode.SIZE_REVERSE -> SizeSortingComparator(false, foldersFirst = true)

                SortingMode.M_TIME_DIRECT -> TimeSortingComparator(true, foldersFirst = true)
                SortingMode.M_TIME_REVERSE -> TimeSortingComparator(true, foldersFirst = true)

                else -> DummySortingComparator()
            }
        }
    }
}


class NameSortingComparator(reverseOrder: Boolean, foldersFirst: Boolean) : FSItemSortingComparator(reverseOrder, foldersFirst) {
    override fun compareItems(item1: FSItem, item2: FSItem): Int {
        return item1.name.compareTo(item2.name)
    }
}


class TimeSortingComparator(reverseOrder: Boolean, foldersFirst: Boolean) : FSItemSortingComparator(reverseOrder, foldersFirst) {
    override fun compareItems(item1: FSItem, item2: FSItem): Int {
        return item1.mTime.compareTo(item2.mTime)
    }
}


class SizeSortingComparator(reverseOrder: Boolean, foldersFirst: Boolean) : FSItemSortingComparator(reverseOrder, foldersFirst) {
    override fun compareItems(item1: FSItem, item2: FSItem): Int {
        return item1.size.compareTo(item2.size)
    }
}


class DummySortingComparator : FSItemSortingComparator(true, true) {
    override fun compareItems(item1: FSItem, item2: FSItem): Int {
        return 0
    }
}