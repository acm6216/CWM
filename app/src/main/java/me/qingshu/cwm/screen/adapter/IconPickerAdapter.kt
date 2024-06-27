package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardIcon

class IconPickerAdapter(
    click:(CardIconItem)->Unit
): PickerAdapter<CardIconViewHolder, CardIconItem>(
    create = {
        CardIconViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class CardIconViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardIconItem,
        click: (CardIconItem) -> Unit
    ){
        binding.label.setText(item.cardIcon.label)
        binding.icon.setImageResource(item.value())
        binding.card.setOnClickListener {
            click.invoke(item)
        }
    }
}

data class CardIconItem(
    override val isCheck:Boolean,
    val cardIcon: CardIcon
):CheckableItemCallback{
    fun value() = if(isCheck) cardIcon.src else cardIcon.uncheck
}