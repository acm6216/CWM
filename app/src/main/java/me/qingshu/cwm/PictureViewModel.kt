package me.qingshu.cwm

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.PictureMarkBinding
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.data.UriExif
import me.qingshu.cwm.data.Exif
import me.qingshu.cwm.data.Logo
import me.qingshu.cwm.databinding.PictureItemBinding
import me.qingshu.cwm.extensions.aperture
import me.qingshu.cwm.extensions.focalLength
import me.qingshu.cwm.extensions.shutter

class PictureViewModel : ViewModel() {

    private val pictureUris = MutableStateFlow<List<UriExif>>(emptyList())
    private val cardColor = MutableStateFlow(CardColor.WHITE)

    private val _message = Channel<Int>(capacity = Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    fun receiveCardColor(color: CardColor) {
        viewModelScope.launch {
            cardColor.emit(color)
        }
    }

    private val cardSize = MutableStateFlow(CardSize.LARGE)
    fun receiveCardSize(size: CardSize) {
        viewModelScope.launch {
            cardSize.emit(size)
        }
    }

    private val cardLogo = MutableStateFlow(Logo.CANNON)
    fun receiveLogo(logo: Logo) {
        viewModelScope.launch {
            cardLogo.emit(logo)
        }
    }

    val useLogo get() = cardLogo.value
    val useCardSize get() = cardSize.value
    val useCardColor get() = cardColor.value

    private val userExif = MutableStateFlow(Exif.empty())
    fun receiveUserExif(exif: Exif) {
        viewModelScope.launch {
            userExif.emit(exif)
        }
    }

    fun useLens(picture: Picture){
        viewModelScope.launch(Dispatchers.IO) {
            if(picture.userExif.lens.isEmpty())
                _message.trySend(R.string.use_lens_message_error)
            else userExif.value.let {
                Exif(
                    device = it.device,
                    lens = picture.userExif.lens,
                    information = it.information
                )
            }.also { userExif.emit(it) }
        }
    }

    fun useDate(picture: Picture){
        viewModelScope.launch(Dispatchers.IO) {
            if(picture.userExif.information.date.isEmpty())
                _message.trySend(R.string.use_date_message_error)
            else userExif.value.let {
                Exif(
                    device = it.device,
                    lens = it.lens,
                    information = Information(
                        date = picture.userExif.information.date,
                        location = it.information.location
                    )
                )
            }.also { userExif.emit(it) }
        }
    }

    fun useDevice(picture: Picture){
        viewModelScope.launch(Dispatchers.IO) {
            if(picture.userExif.device.isEmpty())
                _message.trySend(R.string.use_device_message_error)
            else userExif.value.let {
                Exif(
                    device = picture.userExif.device,
                    lens = it.lens,
                    information = it.information
                )
            }.also { userExif.emit(it) }
        }
    }

    fun useLocation(picture: Picture){
        viewModelScope.launch(Dispatchers.IO) {
            if(picture.userExif.information.location.isEmpty())
                _message.trySend(R.string.use_location_message_error)
            else userExif.value.let {
                Exif(
                    device = it.device,
                    lens = it.lens,
                    information = Information(
                        date = it.information.date,
                        location = picture.userExif.information.location
                    )
                )
            }.also { userExif.emit(it) }
        }
    }

    fun removePicture(picture: Picture){
        if(!saveEnable.value) return
        viewModelScope.launch {
            pictureUris.value.filter {
                picture.uri != it.uri
            }.also {
                pictureUris.emit(it)
            }
        }
    }
    fun removeAllPicture(){
        if(!saveEnable.value) return
        viewModelScope.launch {
            pictureUris.emit(emptyList())
        }
    }

    val previewPictures = combine(
        pictureUris, cardColor, cardSize,
        cardLogo, userExif
    ) { uris, cardColor, cardSize, logo, exif ->
        uris.map {
            val targetDevice = if (it.userExif.device.isEmpty()) exif.device else it.userExif.device
            val targetLens = if (it.userExif.lens.isEmpty()) exif.lens else it.userExif.lens
            val targetInfo = Information.combine(it.userExif.information, exif.information)
            Picture(
                it.uri, cardSize, cardColor,
                logo, Exif(targetDevice, targetLens, targetInfo)
            )
        }
    }

    fun receivePicture(uris: List<Uri>, context: Context) {
        if (uris.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            uris.map { uri ->
                context.contentResolver.openInputStream(uri)!!.use {
                    val exif = ExifInterface(it)
                    val shutter =
                        exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)?.shutter() ?: ""
                    val iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY) ?: ""
                    val date = exif.getAttribute(ExifInterface.TAG_DATETIME) ?: ""
                    val device = exif.getAttribute(ExifInterface.TAG_MODEL) ?: ""
                    val aperture =
                        exif.getAttribute(ExifInterface.TAG_APERTURE_VALUE)?.aperture() ?: ""
                    val focalLength =
                        exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)?.focalLength() ?: ""
                    //val latitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) ?: ""
                    //val longitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) ?: ""
                    UriExif(
                        uri,
                        Exif(
                            device = Device(brand = "", model = device),
                            lens = Lens(
                                paramVisible = false,
                                param = "",
                                focalDistance = focalLength,
                                aperture = aperture,
                                shutter = shutter,
                                iso = iso
                            ),
                            information = Information(date = date, location = "")
                        )
                    )
                }
            }.also {
                pictureUris.emit(it)
            }
        }
    }

    val saveEnable = MutableStateFlow(true)

    fun save(context: Context, binding: PictureItemBinding) {
        val layout = PictureMarkBinding(binding).clear()
        viewModelScope.launch(Dispatchers.IO) {
            saveEnable.emit(false)
            previewPictures.first().forEach {
                val sourceBitmap = context.contentResolver.openInputStream(it.uri)!!.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
                val size = layout.realHeight(sourceBitmap,it)
                layout.exifRoot.layoutParams.apply {
                    height = size
                    width = sourceBitmap.width
                }
                launch(Dispatchers.Main) {
                    layout.setMark(picture = it, isPreview = false, bitmap = sourceBitmap)
                }
                delay(250)
                val bitmap = Bitmap.createBitmap(
                    sourceBitmap.width,
                    size,
                    Bitmap.Config.ARGB_8888
                )
                layout.exifRoot.draw(Canvas(bitmap).apply {
                    drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                })

                val newBitmap = Bitmap.createBitmap(
                    sourceBitmap.width,
                    sourceBitmap.height + bitmap.height,
                    Bitmap.Config.ARGB_8888
                )
                Canvas(newBitmap).apply {
                    drawBitmap(sourceBitmap, 0f, 0f, null)
                    drawBitmap(bitmap, 0f, sourceBitmap.height.toFloat(), null)
                }

                val file = DocumentFile.fromSingleUri(context, it.uri)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver, newBitmap, file?.name?:"", "Camera Water Mark"
                    )
                else context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, file?.name ?: "")
                        put(MediaStore.Images.Media.DESCRIPTION, file?.name ?: "")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }
                )?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { os ->
                        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    }
                }
                delay(250)
            }
            saveEnable.emit(true)
            if(previewPictures.first().isNotEmpty()) _message.trySend(R.string.save_status_completed)
            else _message.trySend(R.string.save_status_error)
        }
    }

}