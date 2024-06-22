package me.qingshu.cwm.data

import android.net.Uri
import me.qingshu.cwm.style.Styles

data class Picture(
    val uri: Uri,
    val cardSize: CardSize,
    val cardColor: CardColor,
    val icon: Logo,
    val userExif: Exif,
    val styles: Styles,
    val artSignature:ArtSignature,
    val gravity:Int = 0,
    val effect:Int = 0,
    val visibleIcon:Boolean = true
){

    fun isShadowPicture():Boolean = (effect and (1 shl 1)) != 0

    fun isShadowText():Boolean = (effect and (1 shl 2)) != 0

    fun isCorner():Boolean = (effect and 1) != 0
}