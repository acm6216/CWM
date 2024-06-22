package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardSize

class SizePickerAdapter(
    click:(CardSize)->Unit
): PickerAdapter<CardSizeViewHolder,CardSizeItem>(
    create = {
        CardSizeViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,checkable)
    },
    CheckableItem(),
    true
)

class CardSizeViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardSizeItem,
        click:(CardSize)->Unit,
        checkable:Boolean
    ){
        binding.label.setText(item.cardSize.label)
        binding.icon.setOnClickListener {
            click.invoke(item.cardSize)
        }

        binding.icon.setImageResource(item.cardSize.src)

        animationVisible(checkable,item.isCheck)
    }

}

data class CardSizeItem(
    override val isCheck:Boolean,
    val cardSize: CardSize
):CheckableItemCallback