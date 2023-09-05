package me.qingshu.cwm.data

import android.net.Uri

data class Picture(
    val uri: Uri,
    val cardSize: CardSize,
    val cardColor: CardColor,
    val logo: Logo,
    val userExif: SimpleExif
)