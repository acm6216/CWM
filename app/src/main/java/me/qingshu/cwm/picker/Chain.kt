package me.qingshu.cwm.picker

import androidx.annotation.ColorInt
import me.qingshu.cwm.picker.ColorAdapter
import me.qingshu.cwm.picker.ColorDecorator

interface Chain {
    fun setColor(caller: ColorDecorator?, @ColorInt color: Int)

    fun setShade(position: Int)

    fun getAdapter(): ColorAdapter?

    fun getAdapterPosition(): Int

    fun getShadePosition(): Int
}