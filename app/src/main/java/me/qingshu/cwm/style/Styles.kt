package me.qingshu.cwm.style

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R
import me.qingshu.cwm.data.Logo

enum class Styles(
    @StringRes val label:Int,
    @DrawableRes val src:Int,
    val visibleLens:Boolean = false,
    val icon: Logo = Logo.SONY,
    val gravityVisible:Boolean = false,
    val effect:Boolean = false,
    val iconVisible:Boolean = false
){
    DEFAULT(R.string.styles_default,visibleLens = true,src = R.drawable.ic_style_default),
    CARD(R.string.styles_card, effect = true,src = R.drawable.ic_style_card),
    INNER(R.string.styles_inner, gravityVisible = true, iconVisible = true,src = R.drawable.ic_style_inner),
    SPACE(R.string.styles_space, effect = true,src = R.drawable.ic_style_space);

}