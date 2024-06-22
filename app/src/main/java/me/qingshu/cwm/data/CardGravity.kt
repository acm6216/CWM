package me.qingshu.cwm.data

import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardGravity(
    @StringRes val label:Int,
    @DrawableRes val src:Int,
    val gravityValue:Int
) {
    NO_GRAVITY(R.string.screen_gravity_none,R.drawable.ic_chevron_left,Gravity.NO_GRAVITY),
    LEFT(R.string.screen_gravity_left,R.drawable.ic_chevron_left,Gravity.START),
    RIGHT(R.string.screen_gravity_right,R.drawable.ic_chevron_right,Gravity.END),
    TOP(R.string.screen_gravity_top,R.drawable.ic_chevron_top,Gravity.TOP),
    BOTTOM(R.string.screen_gravity_bottom,R.drawable.ic_chevron_bottom,Gravity.BOTTOM);

    companion object {

        fun value(gravity: Set<CardGravity>): Int {
            val target = gravity.toMutableList()
            var value = Gravity.NO_GRAVITY
            if (gravity.contains(LEFT) && gravity.contains(RIGHT)) {
                value = value or Gravity.CENTER_HORIZONTAL
                target -= LEFT
                target -= RIGHT
            }
            if (gravity.contains(TOP) && gravity.contains(BOTTOM)) {
                value = value or Gravity.CENTER_VERTICAL
                target -= TOP
                target -= BOTTOM
            }

            target.forEach {
                value = value or it.gravityValue
            }
            return value
        }
    }
}