package com.github.aakumykov.file_lister_navigator_selector.file_lister

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem


abstract class SortingComparator(val directOrder: Boolean, val directoryFirst: Boolean) : Comparator<FSItem> {

}

class NameComparator(directOrder: Boolean, directoryFirst: Boolean) : SortingComparator(directOrder, directoryFirst) {
    override fun compare(o1: FSItem?, o2: FSItem?): Int {

        if (null == o1 || null == o2)
            return 0;

        return if (directOrder) o1.name.compareTo(o2.name) else o2.name.compareTo(o1.name)
    }
}

class DateComparator(directOrder: Boolean, directoryFirst: Boolean) : SortingComparator(directOrder, directoryFirst) {
    override fun compare(o1: FSItem?, o2: FSItem?): Int {

        if (null == o1 || null == o2)
            return 0;

        return if (directOrder) o1.mTime.compareTo(o2.mTime)
        else o2.mTime.compareTo(o1.mTime)
    }
}

class SizeComparator(directOrder: Boolean, directoryFirst: Boolean) : SortingComparator(directOrder, directoryFirst) {
    override fun compare(o1: FSItem?, o2: FSItem?): Int {

        if (null == o1 || null == o2)
            return 0;

        return if (directOrder) o1.size.compareTo(o2.size)
        else o2.size.compareTo(o1.size)
    }
}