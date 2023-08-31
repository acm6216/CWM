package me.qingshu.cwm.extensions

import kotlin.math.ceil

val EMPTY:String get() = ""

fun String.focalLength(): String {
    val a = substring(0, indexOf('/')).toFloat()
    val b = substring(indexOf('/') + 1, length).toFloat()
    return (a / b).toInt().toString()
}

fun String.shutter(): String {
    val v = this.toDouble()
    return if (v > 0) "1/${ceil(1 / v).toInt()}"
    else this
}

fun String.aperture(): String {
    val a = substring(0, indexOf('/')).toFloat()
    val b = substring(indexOf('/') + 1, length).toFloat()
    val result = a / b
    return when {
        result > 1.8f && result < 2f -> "2"
        result > 1.2f && result < 1.5f -> "1.4"
        result > 1.5f && result < 1.9f -> "1.8"
        result > 1f && result < 1.4f -> "1.2"
        result > 4.9 && result < 8 -> "5.6"
        result > 2f && result < 5.6f -> "4"
        result > 5.6f && result <= 8f -> "8"
        result > 8f && result <= 11f -> "11"
        result > 11f && result <= 16f -> "16"
        result > 16f && result <= 22f -> "22"
        result > 22f && result <= 45f -> "45"
        result > 45 -> "64"
        else -> "0.95"
    }
}