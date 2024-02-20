package me.qingshu.cwm.data

import android.content.Context
import android.view.Gravity
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class StyleGravity(
    val flags:Int,
    @StringRes val gravityName:Int
) {
    LEFT_TOP(Gravity.START or Gravity.TOP, R.string.gravity_left_top),
    LEFT_BOTTOM(Gravity.START or Gravity.BOTTOM,R.string.gravity_left_bottom),
    RIGHT_TOP(Gravity.END or Gravity.TOP,R.string.gravity_right_top),
    RIGHT_BOTTOM(Gravity.END or Gravity.BOTTOM,R.string.gravity_right_bottom),
    CENTER_VERTICAL_LEFT(Gravity.CENTER_VERTICAL or Gravity.START,R.string.gravity_center_vertical_left),
    CENTER_VERTICAL_RIGHT(Gravity.CENTER_VERTICAL or Gravity.END,R.string.gravity_center_vertical_right),
    CENTER_HORIZONTAL_TOP(Gravity.CENTER_HORIZONTAL or Gravity.TOP,R.string.gravity_center_horizontal_and_top),
    CENTER_HORIZONTAL_BOTTOM(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,R.string.gravity_center_horizontal_and_bottom),
    CENTER(Gravity.CENTER,R.string.gravity_center);

    fun flagName(context: Context) = context.getString(gravityName)

    companion object{
        fun from(value:Int) = if(value> StyleGravity.values().size || value<0) StyleGravity.values()[0] else StyleGravity.values()[value]
    }
}