package me.qingshu.cwm.data

import android.net.Uri
import me.qingshu.cwm.Logo

data class Picture(
    val uri: Uri,
    val cardSize: CardSize,
    val cardColor: CardColor,
    val logo: Logo,
    val userExif: Exif
)