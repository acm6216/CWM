package me.qingshu.cwm.style.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleCardBinding
import me.qingshu.cwm.extensions.treeObserver
import me.qingshu.cwm.style.StyleViewHolder

class CardStyleViewHolder  private constructor(
    private val binding: CardMarkBinding
) : StyleViewHolder(binding.root) {

    override fun bind(picture: Picture, click: (View, Picture) -> Unit) {
        val src = binding.src
        src.treeObserver {
            binding.setMark(picture,click = click, height = it.height, width = it.width)
        }

        binding.card.radius = if(picture.isCorner()) 8f.dp else  0f

        binding.card.elevation = if(picture.isShadow()) 4f.dp else  0f

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
        fun from(parent: ViewGroup) = CardStyleViewHolder(
            CardMarkBinding(
                StyleCardBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
    }
}