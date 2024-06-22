package me.qingshu.cwm.data

import me.qingshu.cwm.style.Styles

data class Template(
    val device: Device,
    val lens: Lens,
    val information: Information,
    val cardSize: CardSize,
    val cardColor: CardColor,
    val artSignature:ArtSignature,
    val style:Styles,
    val logo: Logo,
    val name:String,
    val key:Long
){
    companion object{
        val empty = Template(
            Device.empty,
            Lens.empty,
            Information.empty,
            CardSize.LARGE,
            CardColor.WHITE,
            ArtSignature.empty,
            Styles.DEFAULT,
            Logo.NIKON,
            "",
            0
        )
    }
}