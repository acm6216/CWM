package me.qingshu.cwm.data

data class UserExif(val device: Device,val lens: Lens,val information: Information){

    companion object{

        private val EMPTY:String get() = ""

        fun empty() = UserExif(
            device = Device(brand = EMPTY, model = EMPTY),
            lens = Lens(param = EMPTY, focalDistance = EMPTY, aperture = EMPTY, shutter = EMPTY, iso = EMPTY),
            information = Information(date = EMPTY, location = EMPTY)
        )
    }
}