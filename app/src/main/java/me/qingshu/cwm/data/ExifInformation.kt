package me.qingshu.cwm.data

import androidx.annotation.StringRes

data class ExifInformation(
    @StringRes val label:Int,
    val value:String
)