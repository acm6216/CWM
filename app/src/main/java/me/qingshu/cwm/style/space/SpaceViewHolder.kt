package me.qingshu.cwm.style.space

import android.view.View
import coil.load
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.extensions.treeObserver
import me.qingshu.cwm.style.StyleViewHolder

class SpaceViewHolder(
    private val binding: SpaceMarkBinding
):StyleViewHolder(binding.root) {
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
        fun from(binding: SpaceMarkBinding) = SpaceViewHolder(binding)
    }
}