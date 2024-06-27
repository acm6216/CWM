package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import me.qingshu.cwm.R
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.extensions.sharedPreferences

class ColorPickerAdapter(
    click:(CardColor)->Unit,
    colorPicker:(def:Int,isTextColor:Boolean,colors:Int,position:Int)->Unit
): PickerAdapter<ColorViewHolder,CardColorCheckableItem>(
    create = {
        ColorViewHolder(it)
    },
    bind = { holder, date, position ->
        holder.bind(date, click,colorPicker,position)
    },
    CheckableItem()
)

class ColorViewHolder(parent:ViewGroup): PickerViewHolder(
    create(parent)
) {

    private val context get() = binding.root.context

    fun bind(
        item: CardColorCheckableItem,
        click: (CardColor) -> Unit,
        colorPicker:(Int,Boolean,Int,Int)->Unit,
        position: Int
    ){
        val defBg = ContextCompat.getColor(context, item.cardColor.bgColor)
        val bgColor = getBgColor(item.cardColor)
        val textColor = getTextColor(item.cardColor)
        binding.card.setOnClickListener {
            when(item.cardColor){
                CardColor.CUSTOM_TEXT -> colorPicker.invoke(defBg,true,bgColor,position)
                CardColor.CUSTOM_BG -> colorPicker.invoke(defBg,false,bgColor,position)
                else -> click.invoke(item.cardColor)
            }
        }
        binding.label.setText(item.cardColor.label)

        check(item.isCheck)

        binding.icon.setImageResource(getIcon(item.cardColor))
        binding.icon.setColorFilter(textColor)
        binding.label.setTextColor(textColor)
        binding.card.checkedIconTint = getColorStateListTest(textColor)
        binding.card.backgroundTintList = getColorStateListTest(bgColor)
        binding.card.rippleColor = getColorStateListTest(textColor).withAlpha(127)
    }
    private fun getIcon(cardColor: CardColor):Int{
        return when(cardColor){
            CardColor.CUSTOM_TEXT,CardColor.CUSTOM_BG -> R.drawable.ic_color
            else -> cardColor.icon
        }
    }
    private fun getTextColor(cardColor:CardColor):Int{
        return when(cardColor){
            CardColor.CUSTOM_BG,CardColor.CUSTOM_TEXT -> getBgColor(cardColor).textColor()
            CardColor.CUSTOM -> context.sharedPreferences().getInt(
                CardColor.CUSTOM_TEXT_COLOR_KEY,
                ContextCompat.getColor(context, cardColor.textColor)
            )
            else -> ContextCompat.getColor(context, cardColor.textColor)
        }
    }
    private fun getBgColor(cardColor:CardColor):Int{
        return when(cardColor) {
            CardColor.CUSTOM_BG,CardColor.CUSTOM -> context.sharedPreferences().getInt(
                CardColor.CUSTOM_BG_COLOR_KEY,
                ContextCompat.getColor(context, cardColor.bgColor)
            )
            CardColor.CUSTOM_TEXT -> context.sharedPreferences().getInt(
                CardColor.CUSTOM_TEXT_COLOR_KEY,
                ContextCompat.getColor(context, cardColor.bgColor)
            )
            else -> ContextCompat.getColor(context, cardColor.bgColor)
        }
    }

    private fun Int.textColor():Int = if(isLight()) 0xff000000.toInt() else 0xffffffff.toInt()
    private fun Int.isLight():Boolean = calculate() >= 0.5f
    private fun Int.calculate():Double = ColorUtils.calculateLuminance(this)
}

data class CardColorCheckableItem(
    override val isCheck:Boolean,
    val cardColor: CardColor
):CheckableItemCallback