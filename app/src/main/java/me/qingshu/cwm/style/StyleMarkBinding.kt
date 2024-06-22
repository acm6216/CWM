package me.qingshu.cwm.style

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.viewbinding.ViewBinding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.extensions.sharedPreferences

/**
 * 设置Exif参数
 */
abstract class StyleMarkBinding<T:ViewBinding>(
    private val _binding:T
) {

    protected val binding get() = _binding
    val root get() = binding.root
    protected val context: Context get() = binding.root.context

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

    protected fun Int.textShadowColor():Int = Color.argb(
        128,
        255-this.red,
        255-this.green,
        255-this.blue
    )
}