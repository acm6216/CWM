package me.qingshu.cwm.binding

import me.qingshu.cwm.R
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.databinding.ParamBinding

class CardColorBinding(private val paramBinding: ParamBinding) {
    private val binding get() = paramBinding.cardColor

    fun bind(select:((CardColor)->Unit)?=null){
        binding.selectRoot.setOnCheckedChangeListener { _, checkedId ->
            select?.invoke(when(checkedId){
                R.id.card_color_white -> CardColor.WHITE
                else -> CardColor.BLACK
            })
        }
    }
}