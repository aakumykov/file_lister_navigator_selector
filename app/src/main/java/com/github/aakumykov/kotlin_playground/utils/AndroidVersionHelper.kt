package com.github.aakumykov.kotlin_playground.utils

import android.os.Build

class AndroidVersionHelper {
    companion object {
        fun is_android_R_or_later(): Boolean
            = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
}