package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import me.qingshu.cwm.data.Font

class ArtSignatureAdapter (
    click:(Font)->Unit
): PickerAdapter<ArtSignatureViewHolder,ArtSignatureItem>(
    create = {
        ArtSignatureViewHolder(it)
    },
    bind = { holder, date, _ ->
        holder.bind(date, click)
    },
    CheckableItem()
)

class ArtSignatureViewHolder(parent: ViewGroup): PickerViewHolder(
    create(parent)
) {

    fun bind(
        item: ArtSignatureItem,
        click: (Font) -> Unit
    ){
        binding.card.setOnClickListener {
            click.invoke(item.font)
        }
        binding.label.setText(item.font.label)
        check(item.isCheck)
    }
}

data class ArtSignatureItem(
    override val isCheck:Boolean,
    val font: Font
):CheckableItemCallback