package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface
import me.qingshu.cwm.extensions.EMPTY
import me.qingshu.cwm.extensions.date
import me.qingshu.cwm.extensions.lensModel

data class Information(val date:String, val location:String){
    fun isEmpty() = date.isEmpty() && location.isEmpty()

    companion object {

        val empty get() = Information(date = EMPTY, location = EMPTY)

        fun combine(a: Information, b: Information) = Information(
            date = a.date.ifEmpty { b.date },
            location = a.location.ifEmpty { b.location }
        )

        fun from(exifInterface: ExifInterface) = Information(exifInterface.date(), exifInterface.lensModel())
    }
}