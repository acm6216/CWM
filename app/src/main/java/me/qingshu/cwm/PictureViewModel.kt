package me.qingshu.cwm

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.FilletBinding.Companion.FILLET_NONE
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.data.UriExif
import me.qingshu.cwm.data.Exif
import me.qingshu.cwm.data.CameraLogo
import me.qingshu.cwm.data.Icon
import me.qingshu.cwm.data.StyleGravity
import me.qingshu.cwm.style.Styles
import me.qingshu.cwm.databinding.PreviewBinding
import me.qingshu.cwm.style.inner.CardInnerBuilder
import me.qingshu.cwm.style.card.CardStyleBuilder
import me.qingshu.cwm.style.def.DefaultStyleBuilder
import me.qingshu.cwm.style.newyear.NewYearStyleBuilder
import me.qingshu.cwm.style.space.SpaceStyleBuilder

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

    private val fillet = MutableStateFlow(FILLET_NONE)
    fun receiveFillet(flags: Int){
        viewModelScope.launch {
            fillet.emit(flags)
        }
    }

    private val cardIcon = MutableStateFlow<Icon>(CameraLogo.CANNON)
    fun receiveLogo(icon: Icon) {
        viewModelScope.launch {
            cardIcon.emit(icon)
        }
    }

    val useLogo get() = cardIcon.value
    val useCardSize get() = cardSize.value
    val useCardColor get() = cardColor.value

    val styles = MutableStateFlow(Styles.DEFAULT)

    fun receiveStyle(style: Styles,fromUser:Boolean){
        viewModelScope.launch {
            if(fromUser) {
                _message.trySend(R.string.save_status_completed)
            }
            styles.emit(style)
        }
    }

    private val styleGravity = MutableStateFlow(StyleGravity.CENTER)
    fun receiveGravity(gravity:StyleGravity){
        viewModelScope.launch {
            styleGravity.emit(gravity)
        }
    }

    private val userExif = MutableStateFlow(Exif.empty())
    fun receiveUserExif(exif: Exif) {
        viewModelScope.launch {
            userExif.emit(exif)
        }
    }

    private val _pictureExif = Channel<ExifInterface>(capacity = Channel.CONFLATED)
    val pictureExif = _pictureExif.receiveAsFlow()
    fun exif(picture: Picture,context: Context){
        viewModelScope.launch {
            context.contentResolver.openInputStream(picture.uri)?.use {
                val exif = ExifInterface(it)
                _pictureExif.trySend(exif)
            }
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
        cardIcon, userExif,styles,styleGravity,fillet
    ) { any ->
        val uris = any[0] as List<UriExif>
        val cardColor = any[1] as CardColor
        val cardSize = any[2] as CardSize
        val icon = any[3] as Icon
        val exif = any[4] as Exif
        val style = any[5] as Styles
        val gravity = any[6] as StyleGravity
        val f = any[7] as Int
        saveEnable.emit(false)
        uris.map { ue ->
            val targetDevice = Device.combine(exif.device,ue.exif.device)
            val targetLens = Lens.combine(exif.lens,ue.exif.lens)
            val targetInfo = Information.combine(exif.information,ue.exif.information)
            Picture(
                ue.uri, cardSize, cardColor,
                icon, Exif(targetDevice, targetLens, targetInfo),
                styles = style,
                gravity = gravity,
                fillet = f
            )
        }.also {
            saveEnable.emit(true)
        }
    }

    fun receivePicture(uris: List<Uri>, context: Context) {
        if (uris.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            uris.map { uri ->
                context.contentResolver.openInputStream(uri)!!.use {
                    UriExif(uri, Exif.from(ExifInterface(it)))
                }
            }.also {
                pictureUris.emit(it)
            }
        }
    }

    val saveEnable = MutableStateFlow(true)

    fun save(context: Context, binding: PreviewBinding) {
        val target = when(styles.value){
            Styles.DEFAULT -> DefaultStyleBuilder(binding.styleDefault)
            Styles.NEW_YEAR -> NewYearStyleBuilder(binding.styleDefault)
            Styles.SPACE -> SpaceStyleBuilder(binding.styleSpace)
            Styles.INNER -> CardInnerBuilder(binding.styleCardInner)
            else -> CardStyleBuilder(binding.styleCard)
        }
        viewModelScope.launch(Dispatchers.IO) {
            previewPictures.first().forEach {
                saveEnable.emit(false)
                target.execute(context,it,this)
            }
            saveEnable.emit(true)
            if(previewPictures.first().isNotEmpty()) _message.trySend(R.string.save_status_completed)
            else _message.trySend(R.string.save_status_error)
        }
    }

}