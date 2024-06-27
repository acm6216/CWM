package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardEffect

class EffectPickerAdapter(
    click:(CardEffect)->Unit
): PickerAdapter<CardEffectViewHolder, EffectItem>(
    create = {
        CardEffectViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class CardEffectViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: EffectItem,
        click: (CardEffect) -> Unit
    ){
        binding.icon.setImageResource(item.cardEffect.src)
        binding.card.setOnClickListener {
            click.invoke(item.cardEffect)
        }
        binding.label.setText(item.cardEffect.label)

        check(item.isCheck)
    }

}

data class EffectItem(
    override val isCheck:Boolean,
    val cardEffect: CardEffect
):CheckableItemCallback