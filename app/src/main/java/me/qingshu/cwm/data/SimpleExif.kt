package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface

data class SimpleExif(val device: Device, val lens: Lens, val information: Information){

    companion object{

        fun from(exifInterface: ExifInterface) = SimpleExif(
            device = Device.from(exifInterface),
            lens = Lens.from(exifInterface),
            information = Information.from(exifInterface)
        )

        fun empty() = SimpleExif(
            device = Device.empty,
            lens = Lens.empty,
            information = Information.empty
        )
    }
}