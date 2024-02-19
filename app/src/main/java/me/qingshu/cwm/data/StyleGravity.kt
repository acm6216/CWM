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
    CENTER(Gravity.CENTER,R.string.gravity_center);

    fun flagName(context: Context) = context.getString(gravityName)

    companion object{
        fun from(value:Int) = if(value> StyleGravity.values().size || value<0) StyleGravity.values()[0] else StyleGravity.values()[value]
    }
}