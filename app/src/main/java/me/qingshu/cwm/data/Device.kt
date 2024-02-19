package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface
import me.qingshu.cwm.extensions.EMPTY
import me.qingshu.cwm.extensions.device

data class Device(val brand:String,val model:String){
    fun string() = "$brand $model".trimStart()

    fun isEmpty() = brand.isEmpty() && model.isEmpty()

    companion object{
        val empty get() = Device(brand = EMPTY, model = EMPTY)

        fun combine(a: Device, b: Device) = Device(
            brand = a.brand.ifEmpty { b.brand },
            model = a.model.ifEmpty { b.model }
        )

        fun from(exifInterface: ExifInterface) = Device(
            brand = EMPTY,
            model = exifInterface.device()
        )
    }
}