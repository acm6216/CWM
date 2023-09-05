package me.qingshu.cwm.extensions

val EMPTY:String get() = ""

/*
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
    val a = substring(0, indexOf('/')).toDouble()
    val b = substring(indexOf('/') + 1, length).toDouble()
    return 2.0.pow(a / b / 2).let {
        DecimalFormat("#.#").format(it)
    }
}*/
