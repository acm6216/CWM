package me.qingshu.cwm.data

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardColor(
    @ColorRes val bgColor:Int,
    @ColorRes val textColor:Int,
    @StringRes val label:Int
) {
    WHITE(R.color.white,R.color.black,R.string.card_color_black),
    BLACK(R.color.black,R.color.white,R.string.card_color_white),
    GREY(R.color.grey,R.color.white,R.string.card_color_grey),
    GOLD(R.color.new_year,R.color.new_year_text,R.string.card_color_gold);
}