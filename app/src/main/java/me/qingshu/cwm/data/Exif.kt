package me.qingshu.cwm.data

data class Exif(val device: Device, val lens: Lens, val information: Information){

    companion object{

        fun empty() = Exif(
            device = Device.empty,
            lens = Lens.empty,
            information = Information.empty
        )
    }
}