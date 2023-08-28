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
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.PictureMarkBinding
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.data.SaveStatus
import me.qingshu.cwm.data.UriExif
import me.qingshu.cwm.data.UserExif
import me.qingshu.cwm.databinding.PictureItemBinding
import kotlin.math.ceil

class PictureViewModel : ViewModel() {

    private val pictureUris = MutableStateFlow<List<UriExif>>(emptyList())
    private val cardColor = MutableStateFlow(CardColor.WHITE)
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

    private val userExif = MutableStateFlow(UserExif.empty())
    fun receiveUserExif(exif: UserExif) {
        viewModelScope.launch {
            userExif.emit(exif)
        }
    }

    fun removePicture(picture: Picture){
        viewModelScope.launch {
            pictureUris.value.filter {
                picture.uri != it.uri
            }.also {
                pictureUris.emit(it)
            }
        }
    }
    fun removeAllPicture(){
        viewModelScope.launch {
            pictureUris.emit(emptyList())
        }
    }

    val previewPictures = combine(
        pictureUris,
        cardColor,
        cardSize,
        cardLogo,
        userExif
    ) { uris, cardColor, cardSize, logo, exif ->
        uris.map {
            val targetDevice = if (it.userExif.device.isEmpty()) exif.device else it.userExif.device
            val targetLens = if (it.userExif.lens.isEmpty()) exif.lens else it.userExif.lens
            val targetInfo = Information.combine(it.userExif.information, exif.information)
            Picture(
                it.uri,
                cardSize,
                cardColor,
                logo,
                UserExif(targetDevice, targetLens, targetInfo)
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
                        UserExif(
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

    val saveStatus = MutableStateFlow(SaveStatus(pictureCount = 0, currentProgress = 0))

    fun save(context: Context, binding: PictureItemBinding,call:()->Unit) {
        val layout = PictureMarkBinding(binding).clear()
        viewModelScope.launch(Dispatchers.IO) {
            saveStatus.emit(SaveStatus(pictureCount = previewPictures.first().size, currentProgress = 0))
            previewPictures.first().forEachIndexed { index, it ->
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
                delay(550)
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
                    ).also { state ->
                        if (state.isNotEmpty()) {
                            Log.d("TAG", "save: 保存成功！")
                        } else {
                            Log.d("TAG", "save: 保存失败！")
                        }
                    }
                else context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, file?.name ?: "")
                        put(MediaStore.Images.Media.DESCRIPTION, file?.name ?: "")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }
                )?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { os ->
                        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os).also { state ->
                            if (state) {
                                Log.d("TAG", "save: 保存成功！")
                            } else {
                                Log.d("TAG", "save: 保存失败！")
                            }
                        }
                    }
                }
                saveStatus.emit(SaveStatus(pictureCount = previewPictures.first().size, currentProgress = index+1))
                delay(250)
            }
            call.invoke()
        }
    }

    private fun String.focalLength(): String {
        val a = substring(0, indexOf('/')).toFloat()
        val b = substring(indexOf('/') + 1, length).toFloat()
        return (a / b).toInt().toString()
    }

    private fun String.shutter(): String {
        val v = this.toDouble()
        return if (v > 0) "1/${ceil(1 / v).toInt()}"
        else this
    }

    private fun String.aperture(): String {
        val a = substring(0, indexOf('/')).toFloat()
        val b = substring(indexOf('/') + 1, length).toFloat()
        val result = a / b
        return when {
            result > 1.8f && result < 2f -> "2"
            result > 1.2f && result < 1.5f -> "1.4"
            result > 1.5f && result < 1.9f -> "1.8"
            result > 1f && result < 1.4f -> "1.2"
            result > 4.9 && result < 8 -> "5.6"
            result > 2f && result < 5.6f -> "4"
            result > 5.6f && result <= 8f -> "8"
            result > 8f && result <= 11f -> "11"
            result > 11f && result <= 16f -> "16"
            result > 16f && result <= 22f -> "22"
            result > 22f && result <= 45f -> "45"
            result > 45 -> "64"
            else -> "0.95"
        }
    }

}