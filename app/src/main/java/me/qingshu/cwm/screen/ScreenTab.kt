package me.qingshu.cwm.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class ScreenTab(
    @StringRes val label:Int,
    @DrawableRes val icon:Int
){
    PICTURE(R.string.screen_picture,R.drawable.ic_picture),
    STYLE(R.string.screen_style,R.drawable.ic_style),
    ICON(R.string.screen_icon,R.drawable.ic_camera),
    EFFECT(R.string.screen_effect,R.drawable.ic_effect),
    COLOR(R.string.screen_color,R.drawable.ic_color),
    SIZE(R.string.screen_size,R.drawable.ic_size),
    GRAVITY(R.string.screen_gravity,R.drawable.ic_gravity),
    TEMPLATE(R.string.screen_template,R.drawable.ic_template);
}