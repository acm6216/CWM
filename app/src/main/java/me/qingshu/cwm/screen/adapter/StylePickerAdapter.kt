package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.style.Styles

class StylePickerAdapter(
    click: (Styles) -> Unit
) : PickerAdapter<CardStylesViewHolder, CardStylesItem>(
    create = {
        CardStylesViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class CardStylesViewHolder(parent: ViewGroup) : PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardStylesItem,
        click: (Styles) -> Unit
    ) {
        binding.icon.setImageResource(item.style.src)
        binding.card.setOnClickListener {
            click.invoke(item.style)
        }
        binding.label.setText(item.style.label)
        check(item.isCheck)
    }
}

data class CardStylesItem(
    override val isCheck: Boolean,
    val style: Styles
):CheckableItemCallback