package me.qingshu.cwm.data

import me.qingshu.cwm.extensions.EMPTY

data class Lens(
    val param: String = EMPTY,
    val focalDistance: String = EMPTY,
    val aperture: String = EMPTY,
    val shutter: String = EMPTY,
    val iso: String = EMPTY,
    val paramVisible:Boolean = true
){
    fun string():String {
        val buffer = StringBuilder()
        if (paramVisible) buffer.append(param)
        else {
            if (focalDistance.isNotEmpty()) buffer.append("${focalDistance}mm ")
            if (aperture.isNotEmpty()) buffer.append("F$aperture ")
            if (shutter.isNotEmpty()) buffer.append("${shutter}S ")
            if (iso.isNotEmpty()) buffer.append("ISO${iso}")
        }
        return buffer.toString()
    }

    fun isEmpty() = param.isEmpty() && focalDistance.isEmpty() && aperture.isEmpty() && shutter.isEmpty() && iso.isEmpty()

    companion object{
        val empty get() = Lens(param = EMPTY, focalDistance = EMPTY, aperture = EMPTY, shutter = EMPTY, iso = EMPTY)
    }
}