package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface Icon{
    @get:StringRes val label: Int
    @get:DrawableRes val src: Int
    val tintEnable: Boolean
    val padding: Int

    fun getNumberOfIcons():Array<out Icon>
    fun compatPadding() = 0
    fun iconPadding() = 0
}