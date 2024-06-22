package me.qingshu.cwm.data

import androidx.exifinterface.media.ExifInterface

data class Exif(
    val device: Device,
    val lens: Lens,
    val information: Information,
    val artSignature: ArtSignature
) {

    companion object {

        fun from(exifInterface: ExifInterface) = Exif(
            device = Device.from(exifInterface),
            lens = Lens.from(exifInterface),
            information = Information.from(exifInterface),
            artSignature = ArtSignature.empty
        )

        fun empty() = Exif(
            device = Device.empty,
            lens = Lens.empty,
            information = Information.empty,
            artSignature = ArtSignature.empty
        )
    }
}