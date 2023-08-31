package me.qingshu.cwm.data

import me.qingshu.cwm.extensions.EMPTY

data class Information(val date:String, val location:String){
    fun isEmpty() = date.isEmpty() && location.isEmpty()

    companion object {

        val empty get() = Information(date = EMPTY, location = EMPTY)

        fun combine(a: Information, b: Information) = Information(
            date = a.date.ifEmpty { b.date },
            location = a.location.ifEmpty { b.location }
        )
    }
}