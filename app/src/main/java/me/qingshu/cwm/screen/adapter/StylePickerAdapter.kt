package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.style.Styles

class StylePickerAdapter(
    click: (Styles) -> Unit
) : PickerAdapter<CardStylesViewHolder, CardStylesItem>(
    create = {
        CardStylesViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date, click, checkable)
    },
    CheckableItem(),
    true
)

class CardStylesViewHolder(parent: ViewGroup) : PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: CardStylesItem,
        click: (Styles) -> Unit,
        checkable: Boolean
    ) {
        binding.icon.setImageResource(item.style.src)
        binding.icon.setOnClickListener {
            click.invoke(item.style)
        }
        binding.label.setText(item.style.label)
        animationVisible(checkable, item.isCheck)
    }
}

data class CardStylesItem(
    override val isCheck: Boolean,
    val style: Styles
):CheckableItemCallback