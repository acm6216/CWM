package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

private const val FILLET_NONE = 0
private const val FILLET_CORNEL = 1
private const val FILLET_SHADOW_PICTURE = 1 shl 1
private const val FILLET_SHADOW_TEXT = 1 shl 2
enum class CardEffect(
    @StringRes val label:Int,
    val flags:Int,
    @DrawableRes val src:Int
) {
    NONE(R.string.screen_effect_none,FILLET_NONE,R.drawable.ic_circle),
    CORNEL(R.string.screen_effect_cornel,FILLET_CORNEL,R.drawable.ic_corner),
    SHADOW_PICTURE(R.string.screen_effect_shadow_picture,FILLET_SHADOW_PICTURE,R.drawable.ic_shadow_picture),
    SHADOW_TEXT(R.string.screen_effect_shadow_text,FILLET_SHADOW_TEXT,R.drawable.ic_shadow_text);

    private fun isShadowPicture(flags:Int):Boolean = (flags and (1 shl 1)) != 0

    private fun isShadowText(flags:Int):Boolean = (flags and (1 shl 2)) != 0

    private fun isCorner(flags: Int):Boolean = (flags and 1) != 0

    companion object{
        fun value(effect: Set<CardEffect>): Int {
            var value = FILLET_NONE
            effect.forEach{
                value = value or it.flags
            }
            return value
        }

    }
}