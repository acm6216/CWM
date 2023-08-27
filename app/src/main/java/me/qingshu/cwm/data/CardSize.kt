package me.qingshu.cwm.data

import android.graphics.Bitmap

enum class CardSize {
    LARGE,MEDIUM,SMALL;

    val size get() = when(this){
        LARGE -> 48
        MEDIUM -> 40
        else -> 32
    }

    val textSize get() =  when(this){
        LARGE -> 8f
        MEDIUM -> 7f
        else -> 6f
    }

    val logoSize get() =  when(this){
        LARGE -> 28
        MEDIUM -> 23
        else -> 16
    }

    private fun base(bitmap: Bitmap) = when(this){
        LARGE -> if(bitmap.height<bitmap.width) 8f else 11f
        MEDIUM -> if(bitmap.height<bitmap.width) 9f else 12f
        else -> if(bitmap.height<bitmap.width) 10f else 13f
    }

    fun sizeByBitmap(bitmap: Bitmap) = (bitmap.height/base(bitmap)).toInt()

    fun textSizeByBitmap(bitmap: Bitmap) = (bitmap.height/base(bitmap)*0.08f).toInt()

    fun logoSizeByBitmap(bitmap: Bitmap) = (bitmap.height/base(bitmap)*0.6f).toInt()

    fun dividerHeightSizeByBitmap(bitmap: Bitmap) =(bitmap.height/base(bitmap)/10f*3f).toInt()

    fun divideWidthSizeByBitmap(bitmap: Bitmap) =(bitmap.height/base(bitmap)/28f).toInt()
}