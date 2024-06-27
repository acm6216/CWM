package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardGravity

class GravityPickerAdapter(
    click:(CardGravity)->Unit
): PickerAdapter<CardGravityViewHolder,CardGravityItem>(
    create = {
        CardGravityViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class CardGravityViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardGravityItem,
        click: (CardGravity) -> Unit
    ){
        binding.label.setText(item.cardGravity.label)
        binding.icon.setImageResource(item.cardGravity.src)
        binding.card.setOnClickListener {
            click.invoke(item.cardGravity)
        }

        check(item.isCheck)
    }
}

data class CardGravityItem(
    override val isCheck:Boolean,
    val cardGravity: CardGravity
):CheckableItemCallback
