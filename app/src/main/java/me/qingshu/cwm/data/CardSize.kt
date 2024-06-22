package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardSize(
    @StringRes val label:Int,
    @DrawableRes val src:Int
) {
    LARGE(R.string.card_size_large,R.drawable.ic_size_large),
    MEDIUM(R.string.card_size_medium,R.drawable.ic_size_medium),
    SMALL(R.string.card_size_small,R.drawable.ic_size_small),
    SUPER_SMALL(R.string.card_size_super_small,R.drawable.ic_size_super_small);

    private fun base(height:Int,width:Int) = when(this){
        LARGE -> if(height<width) 6f else 11f
        MEDIUM -> if(height<width) 7.5f else 12f
        else -> if(height<width) 9f else 13f
    }

    fun sizeByHeight(height:Int,width:Int) = (height/base(height, width)).toInt()

    private fun adjust():Float = if(this==SUPER_SMALL) 0.5f else 1f

    fun textSizeByHeight(height:Int,width:Int) = (height/base(height, width)*0.08f*adjust()).toInt()

    fun logoSizeByHeight(height:Int,width:Int) = (height/base(height, width)*0.6f).toInt()

    fun dividerHeightSizeByHeight(height:Int,width:Int) =(height/base(height, width)/10f*3f).toInt()

    fun dividerWidthSizeByHeight(height:Int, width:Int) =(height/base(height, width)/28f).toInt()
}