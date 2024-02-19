package me.qingshu.cwm.style

import android.content.Context
import android.view.View
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

    protected inline val Int.dp: Int get() = run { toFloat().dp }
    protected inline val Float.dp: Int get() = run {
        val scale: Float = root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }
}