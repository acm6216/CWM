package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardIcon(
    @StringRes val label:Int,
    @DrawableRes val src:Int,
    @DrawableRes val uncheck:Int = src
) {
    GONE(R.string.screen_icon_gone,R.drawable.ic_visibility_on,R.drawable.ic_visibility_off),
    LOGO(R.string.screen_icon_logo,R.drawable.ic_camera),
    WORD_ART(R.string.screen_icon_art_signature,R.drawable.ic_word_art);
}