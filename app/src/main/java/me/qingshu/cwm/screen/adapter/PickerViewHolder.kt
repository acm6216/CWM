package me.qingshu.cwm.screen.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.databinding.ChoiceItemBinding

open class PickerViewHolder(
    protected val binding: ChoiceItemBinding
): RecyclerView.ViewHolder(
    binding.root
) {

    fun animationVisible(checkable:Boolean,isCheck:Boolean){
        binding.card.isChecked = isCheck
    }

    protected fun getColorStateListTest(color:Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_pressed)
        )
        val colors = intArrayOf(color, color, color, color)
        return ColorStateList(states, colors)
    }

    private inline val Int.dp: Int get() = run { toFloat().dp }
    private inline val Float.dp: Int get() = run {
        val scale: Float = binding.root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

    companion object{

        fun create(parent: ViewGroup) =
            ChoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    }
}