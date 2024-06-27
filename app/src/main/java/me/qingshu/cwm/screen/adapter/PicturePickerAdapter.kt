package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.CardMenu

class MenuPickerAdapter(
    click:(MainMenuItem)->Unit
): PickerAdapter<MenuViewHolder,MainMenuItem>(
    create = {
        MenuViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class MenuViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: MainMenuItem,
        click: (MainMenuItem) -> Unit
    ){
        binding.label.setText(item.cardMenu.label)
        binding.card.isEnabled = when(item.cardMenu){
            CardMenu.SAVE -> item.isCheck
            else -> true
        }
        binding.icon.setImageResource(item.cardMenu.icon)
        binding.card.setOnClickListener {
            click.invoke(item)
        }
        check(false)
    }
}

data class MainMenuItem(
    override val isCheck:Boolean,
    val cardMenu: CardMenu
):CheckableItemCallback