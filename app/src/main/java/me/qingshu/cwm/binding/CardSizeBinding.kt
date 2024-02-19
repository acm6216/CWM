package me.qingshu.cwm.binding

import me.qingshu.cwm.R
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceCardSizeBinding

class CardSizeBinding(paramBinding: ParamBinding):Binding<PreferenceCardSizeBinding>(paramBinding) {

    override val binding get() = get { it.cardSize }

    fun bind(select:((CardSize)->Unit)?=null){
        binding.selectRoot.setOnCheckedChangeListener { _, checkedId ->
            select?.invoke(when(checkedId){
                R.id.card_size_large -> CardSize.LARGE
                R.id.card_size_medium -> CardSize.MEDIUM
                else -> CardSize.SMALL
            })
        }
    }

    fun setCardSize(cardSize: CardSize){
        when(cardSize){
            CardSize.LARGE -> R.id.card_size_large
            CardSize.MEDIUM -> R.id.card_size_medium
            else -> R.id.card_size_small
        }.also {
            binding.selectRoot.check(it)
        }
    }
}