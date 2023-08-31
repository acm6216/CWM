package me.qingshu.cwm.data

data class Template(
    val logo: Logo,
    val device: Device,
    val lens: Lens,
    val information: Information,
    val cardSize: CardSize,
    val cardColor: CardColor
){
    companion object{
        val MAX get() = 6
        val USE = ArrayList<Template>()
    }
}