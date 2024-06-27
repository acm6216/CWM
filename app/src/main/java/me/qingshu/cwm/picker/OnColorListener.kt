package me.qingshu.cwm.picker

import androidx.annotation.ColorInt

interface OnColorListener {
    fun onColorChanged(@ColorInt color: Int)
    fun onColorSelected(@ColorInt color: Int)
}