package me.qingshu.cwm.style

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.data.Picture

/**
 * 绑定数据
 */
abstract class StyleViewHolder(
    protected val view: View
): RecyclerView.ViewHolder(view) {
    abstract fun bind(picture: Picture, click: (View, Picture) -> Unit)

    protected inline val Float.dp: Float get() = run {
        val scale: Float = view.resources.displayMetrics.density
        (this * scale + 0.5f)
    }
}