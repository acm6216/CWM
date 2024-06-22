package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardEffect

class EffectPickerAdapter(
    click:(CardEffect)->Unit
): PickerAdapter<CardEffectViewHolder, EffectItem>(
    create = {
        CardEffectViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,checkable)
    },
    CheckableItem(),
    true
)

class CardEffectViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: EffectItem,
        click:(CardEffect)->Unit,
        checkable:Boolean
    ){
        binding.icon.setImageResource(item.cardEffect.src)
        binding.icon.setOnClickListener {
            click.invoke(item.cardEffect)
        }
        binding.label.setText(item.cardEffect.label)

        animationVisible(checkable,item.isCheck)
    }

}

data class EffectItem(
    override val isCheck:Boolean,
    val cardEffect: CardEffect
):CheckableItemCallback