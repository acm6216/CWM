package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardSize

class SizePickerAdapter(
    click:(CardSize)->Unit
): PickerAdapter<CardSizeViewHolder,CardSizeItem>(
    create = {
        CardSizeViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class CardSizeViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardSizeItem,
        click: (CardSize) -> Unit
    ){
        binding.label.setText(item.cardSize.label)
        binding.card.setOnClickListener {
            click.invoke(item.cardSize)
        }

        binding.icon.setImageResource(item.cardSize.src)

        check(item.isCheck)
    }

}

data class CardSizeItem(
    override val isCheck:Boolean,
    val cardSize: CardSize
):CheckableItemCallback