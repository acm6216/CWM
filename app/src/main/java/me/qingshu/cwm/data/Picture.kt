package me.qingshu.cwm.data

import android.net.Uri
import me.qingshu.cwm.style.Styles

data class Picture(
    val uri: Uri,
    val cardSize: CardSize,
    val cardColor: CardColor,
    val icon: Icon,
    val userExif: Exif,
    val styles: Styles,
    val gravity:StyleGravity = StyleGravity.CENTER,
    val fillet:Int = 0,
    val visible:Boolean = true
){

    fun isShadow():Boolean = (fillet and (1 shl 1)) != 0

    fun isCorner():Boolean = (fillet and 1) != 0
}