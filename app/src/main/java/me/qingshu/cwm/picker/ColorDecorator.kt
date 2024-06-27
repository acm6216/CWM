package me.qingshu.cwm.picker

import androidx.annotation.ColorInt

interface ColorDecorator {
    fun onColorChanged(chain: Chain?, @ColorInt color: Int)
}