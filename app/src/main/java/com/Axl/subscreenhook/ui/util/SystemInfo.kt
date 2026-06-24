package com.Axl.subscreenhook.ui.util

import android.os.Build

internal fun currentDeviceName(): String {
    return "${Build.MANUFACTURER} ${Build.MODEL}"
}

internal fun currentSystemVersion(): String {
    return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
}
