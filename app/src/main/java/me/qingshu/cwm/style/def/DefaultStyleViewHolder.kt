package me.qingshu.cwm.style.def

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.extensions.treeObserver
import me.qingshu.cwm.style.StyleViewHolder

class DefaultStyleViewHolder private constructor(
    private val binding: DefaultMarkBinding
) : StyleViewHolder(binding.root) {

    override fun bind(picture: Picture, click: (View, Picture) -> Unit) {
        val src = binding.src
        src.treeObserver {
            binding.setMark(picture,click = click, height = it.height, width = it.width)
        }

        src.load(picture.uri) {
            crossfade(true)
            target {
                val width = src.width.toFloat()
                val height = it.intrinsicHeight*width/it.intrinsicWidth
                src.setImageDrawable(it)
                binding.setMark(picture,click = click, height = height.toInt(), width = width.toInt())
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = DefaultStyleViewHolder(
            DefaultMarkBinding(
                StyleDefaultBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
    }
}