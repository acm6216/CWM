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
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.data.UriExif
import me.qingshu.cwm.data.SimpleExif
import me.qingshu.cwm.data.Logo
import me.qingshu.cwm.databinding.PictureItemBinding

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

    private val userExif = MutableStateFlow(SimpleExif.empty())
    fun receiveUserExif(simpleExif: SimpleExif) {
        viewModelScope.launch {
            userExif.emit(simpleExif)
        }
    }

    fun useLens(picture: Picture){
        viewModelScope.launch(Dispatchers.IO) {
            if(picture.userExif.lens.isEmpty())
                _message.trySend(R.string.use_lens_message_error)
            else userExif.value.let {
                SimpleExif(
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
                SimpleExif(
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
                SimpleExif(
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
                SimpleExif(
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
            val targetDevice = if (it.exif.device.isEmpty()) exif.device else it.exif.device
            val targetLens = if (it.exif.lens.isEmpty()) exif.lens else it.exif.lens
            val targetInfo = Information.combine(exif.information,it.exif.information)
            Picture(
                it.uri, cardSize, cardColor,
                logo, SimpleExif(targetDevice, targetLens, targetInfo)
            )
        }
    }

    fun receivePicture(uris: List<Uri>, context: Context) {
        if (uris.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            uris.map { uri ->
                context.contentResolver.openInputStream(uri)!!.use {
                    UriExif(uri, SimpleExif.from(ExifInterface(it)))
                }
            }.also {
                pictureUris.emit(it)
            }
        }
    }

    val saveEnable = MutableStateFlow(true)

    @Suppress("DEPRECATION")
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
                    layout.setMark(picture = it, height = sourceBitmap.height, width = sourceBitmap.width)
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