package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface

data class Exif(
    val device: Device,
    val lens: Lens,
    val information: Information
) {

    companion object {

        fun from(exifInterface: ExifInterface) = Exif(
            device = Device.from(exifInterface),
            lens = Lens.from(exifInterface),
            information = Information.from(exifInterface)
        )

        fun empty() = Exif(
            device = Device.empty,
            lens = Lens.empty,
            information = Information.empty
        )
    }
}