package me.qingshu.cwm.data

enum class CardSize {
    LARGE,MEDIUM,SMALL;

    val size get() = when(this){
        LARGE -> 48
        MEDIUM -> 40
        else -> 32
    }

    private fun base(height:Int,width:Int) = when(this){
        LARGE -> if(height<width) 6f else 11f
        MEDIUM -> if(height<width) 7.5f else 12f
        else -> if(height<width) 9f else 13f
    }

    fun sizeByHeight(height:Int,width:Int) = (height/base(height, width)).toInt()

    fun textSizeByHeight(height:Int,width:Int) = (height/base(height, width)*0.08f).toInt()

    fun logoSizeByHeight(height:Int,width:Int) = (height/base(height, width)*0.6f).toInt()

    fun dividerHeightSizeByHeight(height:Int,width:Int) =(height/base(height, width)/10f*3f).toInt()

    fun divideWidthSizeByHeight(height:Int,width:Int) =(height/base(height, width)/28f).toInt()
}