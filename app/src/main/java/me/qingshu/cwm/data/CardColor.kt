package me.qingshu.cwm.data

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R
import me.qingshu.cwm.extensions.defaultSharedPreferences
import me.qingshu.cwm.extensions.edit

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
    CUSTOM_BACKGROUND(R.color.white,R.color.black,R.string.card_color_custom_bg);

    fun fromPalette() = this == AUTO || this == BLUR



    companion object{

        private const val CUSTOM_TEXT_COLOR_KEY = "CUSTOM_TEXT_COLOR_KEY"
        private const val CUSTOM_BG_COLOR_KEY = "CUSTOM_BG_COLOR_KEY"

        fun getCustomTextColor(def:Int, context: Context) = defaultSharedPreferences(context).getInt(CUSTOM_TEXT_COLOR_KEY,def)

        fun getCustomBackgroundColor(def:Int, context: Context) = defaultSharedPreferences(context).getInt(CUSTOM_BG_COLOR_KEY,def)

        fun storeCustomTextColor(context: Context, value:Int, unit:()->Unit){
            defaultSharedPreferences(context).edit {
                putInt(CUSTOM_TEXT_COLOR_KEY,value)
            }
            unit.invoke()
        }

        fun storeCustomBackgroundColor(context: Context, value:Int, unit:()->Unit){
            defaultSharedPreferences(context).edit {
                putInt(CUSTOM_BG_COLOR_KEY,value)
            }
            unit.invoke()
        }
    }
}