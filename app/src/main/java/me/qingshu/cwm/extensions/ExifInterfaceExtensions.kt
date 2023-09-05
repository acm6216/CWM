package me.qingshu.cwm.extensions

import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.pow

fun ExifInterface.shutter() =
    getAttribute(ExifInterface.TAG_EXPOSURE_TIME)?.let {
        val v = it.toDouble()
        return if (v > 0) "1/${ceil(1 / v).toInt()}"
        else it
    } ?: ""

fun ExifInterface.aperture() = getAttribute(ExifInterface.TAG_APERTURE_VALUE)?.let {
    val a = it.substring(0, it.indexOf('/')).toDouble()
    val b = it.substring(it.indexOf('/') + 1, it.length).toDouble()
    return 2.0.pow(a / b / 2).let { v ->
        DecimalFormat("#.#").format(v).let { s ->
            Log.d("TAG", "aperture: $s,$v")
            when(s){
                "5.7" -> "5.6"
                else -> s
            }
        }
    }
} ?: ""

fun ExifInterface.focalLength() = getAttribute(ExifInterface.TAG_FOCAL_LENGTH)?.let {
    val a = it.substring(0, it.indexOf('/')).toFloat()
    val b = it.substring(it.indexOf('/') + 1, it.length).toFloat()
    location()
    return (a / b).toInt().toString()
} ?: ""

fun ExifInterface.iso() = getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY) ?: ""

fun ExifInterface.date() = getAttribute(ExifInterface.TAG_DATETIME) ?: ""

fun ExifInterface.device() = getAttribute(ExifInterface.TAG_MODEL) ?: ""

fun ExifInterface.lensModel() = getAttribute(ExifInterface.TAG_LENS_MODEL)?:""

fun ExifInterface.location(){
    getAttribute(ExifInterface.TAG_GPS_LONGITUDE).also {
        Log.d("TAG", "aperture: location = $it")
    }
}