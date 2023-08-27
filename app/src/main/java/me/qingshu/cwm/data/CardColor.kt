package me.qingshu.cwm.data

import androidx.annotation.ColorRes
import me.qingshu.cwm.R

enum class CardColor(@ColorRes val color:Int) {
    WHITE(R.color.white),
    BLACK(R.color.black);

    fun textColor() = when(this){
        WHITE -> R.color.black
        else -> R.color.white
    }
}