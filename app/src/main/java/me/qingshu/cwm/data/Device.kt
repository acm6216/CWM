package me.qingshu.cwm.data

import me.qingshu.cwm.extensions.EMPTY

data class Device(val brand:String,val model:String){
    fun string() = "$brand $model".trimStart()

    fun isEmpty() = brand.isEmpty() && model.isEmpty()

    companion object{
        val empty get() = Device(brand = EMPTY, model = EMPTY)
    }
}