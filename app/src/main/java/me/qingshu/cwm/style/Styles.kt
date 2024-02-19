package me.qingshu.cwm.style

import android.content.Context
import androidx.annotation.StringRes
import me.qingshu.cwm.R
import me.qingshu.cwm.data.CameraLogo
import me.qingshu.cwm.data.FestivalLogo
import me.qingshu.cwm.data.Icon

enum class Styles(
    @StringRes val id:Int,
    val visibleLens:Boolean = false,
    val icon: Icon = CameraLogo.SONY,
    val gravityVisible:Boolean = false,
    val fillet:Boolean = false
){
    DEFAULT(R.string.styles_default,visibleLens = true),
    CARD(R.string.styles_card, fillet = true),
    INNER(R.string.styles_inner, gravityVisible = true),
    NEW_YEAR(R.string.styles_new_year,visibleLens = true, icon = FestivalLogo.CHINESE_NEW_YEAR),
    SPACE(R.string.styles_space,visibleLens = true, fillet = true);

    fun styleName(context: Context) = context.getString(id)

    companion object{
        fun from(value:Int) = if(value> values().size || value<0) values()[0] else values()[value]
    }
}