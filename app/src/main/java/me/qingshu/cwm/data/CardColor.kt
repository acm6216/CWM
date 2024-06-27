package me.qingshu.cwm.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardColor(
    @ColorRes val bgColor:Int,
    @ColorRes val textColor:Int,
    @StringRes val label:Int,
    @DrawableRes val icon:Int = R.drawable.ic_circle
) {
    BLUR(R.color.checked,R.color.white,R.string.card_color_blur,R.drawable.ic_blur),
    AUTO(R.color.checked,R.color.white,R.string.card_color_auto,R.drawable.ic_auto_fix),
    WHITE(R.color.white,R.color.black,R.string.card_color_white),
    BLACK(R.color.black,R.color.white,R.string.card_color_black),
    GREY(R.color.grey,R.color.white,R.string.card_color_grey),
    GOLD(R.color.new_year,R.color.new_year_text,R.string.card_color_gold),
    CUSTOM(R.color.white,R.color.black,R.string.card_color_custom),
    CUSTOM_TEXT(R.color.black,R.color.white,R.string.card_color_custom_text),
    CUSTOM_BG(R.color.white,R.color.black,R.string.card_color_custom_bg);

    fun fromPalette() = this == AUTO || this == BLUR

    companion object{
        const val CUSTOM_TEXT_COLOR_KEY = "CUSTOM_TEXT_COLOR_KEY"
        const val CUSTOM_BG_COLOR_KEY = "CUSTOM_BG_COLOR_KEY"
    }
}