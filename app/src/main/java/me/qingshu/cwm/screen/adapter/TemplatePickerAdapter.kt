package me.qingshu.cwm.screen.adapter

import android.view.View
import android.view.ViewGroup
import me.qingshu.cwm.R
import me.qingshu.cwm.data.Template

class TemplatePickerAdapter(
    click:(Template)->Unit,
    longClick:(View, Template)->Unit
):PickerAdapter<TemplateViewHolder,TemplateItem>(
    create = {
        TemplateViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,longClick,checkable)
    },
    CheckableItem(),
    true
)

class TemplateViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item:TemplateItem,
        click:(Template)->Unit,
        longClick:(View, Template)->Unit,
        checkable:Boolean
    ){
        binding.card.setOnClickListener { click.invoke(item.template) }
        binding.card.setOnLongClickListener {
            longClick.invoke(it,item.template)
            true
        }
        binding.icon.setImageResource(R.drawable.ic_template)
        binding.label.text = item.template.name
        animationVisible(checkable,item.isCheck)
    }
}

data class TemplateItem(
    override val isCheck:Boolean,
    val template: Template
):CheckableItemCallback