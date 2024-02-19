package me.qingshu.cwm.binding

import me.qingshu.cwm.R
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceCardColorBinding

class CardColorBinding(
    paramBinding: ParamBinding
):Binding<PreferenceCardColorBinding>(paramBinding) {
    override val binding get() = get { it.cardColor }

    fun bind(select:((CardColor)->Unit)?=null){
        binding.selectRoot.setOnCheckedChangeListener { _, checkedId ->
            select?.invoke(when(checkedId){
                R.id.card_color_white -> CardColor.WHITE
                R.id.card_color_grey -> CardColor.GREY
                R.id.card_color_gold -> CardColor.GOLD
                else -> CardColor.BLACK
            })
        }
    }

    fun setCardColor(cardColor: CardColor){
        when(cardColor){
            CardColor.WHITE -> R.id.card_color_white
            CardColor.GREY -> R.id.card_color_grey
            CardColor.GOLD -> R.id.card_color_gold
            else -> R.id.card_color_black
        }.also {
            binding.selectRoot.check(it)
        }
    }
}