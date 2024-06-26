package me.qingshu.cwm.data

import android.graphics.Bitmap
import android.net.Uri

data class BlurUri(
    val uri: Uri,
    val blur: Bitmap?
)