package me.qingshu.cwm.data

data class Device(val brand:String,val model:String){
    fun string() = "$brand $model".trimStart()

    fun isEmpty() = brand.isEmpty() && model.isEmpty()
}