package me.qingshu.cwm.data

data class Information(val date:String, val location:String){
    fun isEmpty() = date.isEmpty() && location.isEmpty()

    companion object {
        fun combine(a: Information, b: Information) = Information(
            date = a.date.ifEmpty { b.date },
            location = a.location.ifEmpty { b.location }
        )
    }
}