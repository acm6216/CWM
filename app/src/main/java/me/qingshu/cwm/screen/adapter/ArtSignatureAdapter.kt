package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.Font

class ArtSignatureAdapter (
    click:(Font)->Unit
): PickerAdapter<ArtSignatureViewHolder,ArtSignatureItem>(
    create = {
        ArtSignatureViewHolder(it)
    },
    bind = { holder, date, _, checkable ->
        holder.bind(date,click,checkable)
    },
    CheckableItem(),
    true
)

class ArtSignatureViewHolder(parent: ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item:ArtSignatureItem,
        click:(Font)->Unit,
        checkable:Boolean
    ){
        binding.icon.setOnClickListener {
            click.invoke(item.font)
        }
        binding.label.setText(item.font.label)
        animationVisible(checkable,item.isCheck)
    }
}

data class ArtSignatureItem(
    override val isCheck:Boolean,
    val font: Font
):CheckableItemCallback