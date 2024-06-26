package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import me.qingshu.cwm.data.CardColor

class ColorPickerAdapter(
    click:(CardColor)->Unit
): PickerAdapter<ColorViewHolder,CardColorCheckableItem>(
    create = {
        ColorViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,checkable)
    },
    CheckableItem(),
    true
)

class ColorViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    private val context get() = binding.root.context

    fun bind(
        item:CardColorCheckableItem,
        click:(CardColor)->Unit,
        checkable:Boolean
    ){
        binding.card.setOnClickListener {
            click.invoke(item.cardColor)
        }
        binding.label.setText(item.cardColor.label)

        animationVisible(checkable,item.isCheck)

        val bgColor = ContextCompat.getColor(context, item.cardColor.bgColor)
        val textColor = ContextCompat.getColor(context, item.cardColor.textColor)
        binding.icon.setImageResource(item.cardColor.icon)
        binding.icon.setColorFilter(textColor)
        binding.label.setTextColor(textColor)
        binding.card.checkedIconTint = getColorStateListTest(textColor)
        binding.card.backgroundTintList = getColorStateListTest(bgColor)
        binding.card.rippleColor = getColorStateListTest(textColor).withAlpha(127)
    }
}

data class CardColorCheckableItem(
    override val isCheck:Boolean,
    val cardColor: CardColor
):CheckableItemCallback