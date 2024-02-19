package me.qingshu.cwm.data

data class Template(
    //val icon: Icon,
    val device: Device,
    val lens: Lens,
    val information: Information,
    val cardSize: CardSize,
    val cardColor: CardColor
){
    companion object{
        val MAX get() = 8
        val USE = ArrayList<Template>()
    }
}