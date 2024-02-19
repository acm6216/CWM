package me.qingshu.cwm.data

import androidx.annotation.ColorRes
import me.qingshu.cwm.R

enum class CardColor(@ColorRes val bgColor:Int, @ColorRes val textColor:Int) {
    WHITE(R.color.white,R.color.black),
    BLACK(R.color.black,R.color.white),
    GREY(R.color.grey,R.color.white),
    GOLD(R.color.new_year,R.color.new_year_text);
}