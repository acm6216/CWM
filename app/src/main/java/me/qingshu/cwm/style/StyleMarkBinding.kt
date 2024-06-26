package me.qingshu.cwm.style

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import androidx.viewbinding.ViewBinding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.extensions.sharedPreferences
import me.qingshu.cwm.view.SpaceTextView

/**
 * 设置Exif参数
 */
abstract class StyleMarkBinding<T:ViewBinding>(
    private val _binding:T
) {

    protected val emptyString = ""

    protected val binding get() = _binding
    val root get() = binding.root
    protected val context: Context get() = binding.root.context

    fun getTextColor(picture: Picture):Int{
        val def = ContextCompat.getColor(context,picture.cardColor.textColor)
        return when{
            picture.isPalette() && !picture.isBlur() -> picture.palette?.switch()?.bodyTextColor ?: def
            picture.isBlur() -> picture.palette?.switch()?.titleTextColor ?: def
            else -> def
        }
    }

    fun getBgColor(picture: Picture):Int{
        val def = ContextCompat.getColor(context,picture.cardColor.bgColor)
        return when{
            picture.isPalette() && !picture.isBlur() -> picture.palette?.switch()?.rgb ?: def
            picture.isBlur() -> 0
            else -> def
        }
    }

    private fun Palette.switch():Swatch?{
        return lightVibrantSwatch?:swatches.find { it!=null }
    }

    protected fun sharedPreferences() = context.sharedPreferences()

    abstract fun clear():StyleMarkBinding<T>

    abstract fun setMark(
        picture: Picture,
        height:Int,
        width:Int,
        click: ((View, Picture) -> Unit)? = null
    ):Any

    fun typeface():Typeface = Typeface.createFromAsset(context.assets,"exif.otf")

    protected inline val Int.dp: Int get() = run { toFloat().dp }
    protected inline val Float.dp: Int get() = run {
        val scale: Float = root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

    private fun Int.textShadowColor():Int = Color.argb(
        128,
        255-this.red,
        255-this.green,
        255-this.blue
    )

    protected fun TextView.shadow(textSize:Float, textColor:Int,picture: Picture){
        setShadowLayer(if(picture.isShadowText()) textSize/5 else 0f,textSize/6,textSize/10,textColor.textShadowColor())
    }

    protected fun SpaceTextView.shadow(textSize:Float, textColor:Int,picture: Picture){
        setShadowLayer(if(picture.isShadowText()) textSize/5 else 0f,textSize/6,textSize/10,textColor.textShadowColor())
    }
}