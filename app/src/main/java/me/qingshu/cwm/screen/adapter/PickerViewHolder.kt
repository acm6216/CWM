package me.qingshu.cwm.screen.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.databinding.SingleChoiceItemBinding

open class PickerViewHolder(
    protected val binding: SingleChoiceItemBinding
): RecyclerView.ViewHolder(
    binding.root
) {

    fun animationVisible(checkable:Boolean,isCheck:Boolean){
        if(checkable){
            if(isCheck) {
                AlphaAnimation(0f,1f).also {
                    it.duration = 150
                    binding.checked.startAnimation(it)
                    binding.checked.visibility = View.VISIBLE
                }
            }
            else {
                AlphaAnimation(1f,0f).also {
                    binding.checked.visibility = View.VISIBLE
                    it.duration = 150
                    it.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationStart(p0: Animation?) {

                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            binding.checked.visibility = View.INVISIBLE
                        }

                        override fun onAnimationRepeat(p0: Animation?) {

                        }
                    })
                    binding.checked.startAnimation(it)
                }
            }
        }else binding.checked.visibility = View.GONE
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
            SingleChoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    }
}