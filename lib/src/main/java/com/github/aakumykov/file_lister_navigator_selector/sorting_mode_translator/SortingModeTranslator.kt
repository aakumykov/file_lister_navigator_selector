package com.github.aakumykov.file_lister_navigator_selector.sorting_mode_translator

interface SortingModeTranslator<SortingModeType> {

    fun sortingModeNames(reverseOrder: Boolean, selectedITem: SortingModeType): Array<String>
    fun sortingNameToSortingMode(name: String): SortingModeType?

    fun sortingModeToPosition(mode: SortingModeType): Int
    fun positionToSortingMode(position: Int): SortingModeType
}