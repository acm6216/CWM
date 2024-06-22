package me.qingshu.cwm.screen.adapter

import android.view.View
import android.view.ViewGroup
import me.qingshu.cwm.data.CardMenu

class MenuPickerAdapter(
    click:(MainMenuItem)->Unit
): PickerAdapter<MenuViewHolder,MainMenuItem>(
    create = {
        MenuViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,checkable)
    },
    CheckableItem()
)

class MenuViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: MainMenuItem,
        click:(MainMenuItem)->Unit,
        checkable:Boolean
    ){
        binding.label.setText(item.cardMenu.label)
        binding.icon.isEnabled = when(item.cardMenu){
            CardMenu.SAVE -> item.isCheck
            else -> true
        }
        binding.icon.setImageResource(item.cardMenu.icon)
        binding.icon.setOnClickListener {
            click.invoke(item)
        }

        if (checkable) binding.checked.visibility = View.VISIBLE
        else binding.checked.visibility = View.GONE
    }
}

data class MainMenuItem(
    override val isCheck:Boolean,
    val cardMenu: CardMenu
):CheckableItemCallback