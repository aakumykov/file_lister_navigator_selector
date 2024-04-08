package com.github.aakumykov.file_lister_navigator_selector.file_lister

@Deprecated("Переименовать в SimpleSortingMode")
enum class SortingMode {
    NAME_DIRECT,
    NAME_REVERSE,

    C_TIME_DIRECT,
    C_TIME_REVERSE,

    M_TIME_DIRECT,
    M_TIME_REVERSE,

    SIZE_DIRECT,
    SIZE_REVERSE
}