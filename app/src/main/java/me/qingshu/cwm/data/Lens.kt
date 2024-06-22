package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface
import me.qingshu.cwm.extensions.EMPTY
import me.qingshu.cwm.extensions.aperture
import me.qingshu.cwm.extensions.focalLength
import me.qingshu.cwm.extensions.iso
import me.qingshu.cwm.extensions.shutter

data class Lens(
    val param: String = EMPTY,
    val focalDistance: String = EMPTY,
    val aperture: String = EMPTY,
    val shutter: String = EMPTY,
    val iso: String = EMPTY,
    val paramVisible:Boolean = false
){
    fun string():String {
        val buffer = StringBuilder()
        if (paramVisible) buffer.append(param)
        else {
            if (focalDistance.isNotEmpty()) buffer.append("${focalDistance}mm ")
            if (aperture.isNotEmpty()) buffer.append("f/$aperture ")
            if (shutter.isNotEmpty()) buffer.append("${shutter}S ")
            if (iso.isNotEmpty()) buffer.append("ISO${iso}")
        }
        return buffer.toString()
    }

    companion object{
        val empty get() = Lens(param = EMPTY, focalDistance = EMPTY, aperture = EMPTY, shutter = EMPTY, iso = EMPTY)

        fun combine(a:Lens,b:Lens) = Lens(
            param = a.param.ifEmpty { b.param },
            paramVisible = a.paramVisible,
            iso = a.iso.ifEmpty { b.iso },
            shutter = a.shutter.ifEmpty { b.shutter },
            focalDistance = a.focalDistance.ifEmpty { b.focalDistance },
            aperture = a.aperture.ifEmpty { b.aperture }
        )

        fun from(exifInterface: ExifInterface) = Lens(
            paramVisible = false,
            param = EMPTY,
            iso = exifInterface.iso(),
            shutter = exifInterface.shutter(),
            aperture = exifInterface.aperture(),
            focalDistance = exifInterface.focalLength()
        )
    }
}