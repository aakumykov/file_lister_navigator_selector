package com.github.aakumykov.storage_access_helper.storage_access_helper

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R