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
import me.qingshu.cwm.data.Logo
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardEffect
import me.qingshu.cwm.data.CardGravity
import me.qingshu.cwm.data.CardIcon
import me.qingshu.cwm.data.CardMenu
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Exif
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.data.Template
import me.qingshu.cwm.data.UriExif
import me.qingshu.cwm.data.ArtSignature
import me.qingshu.cwm.data.Font
import me.qingshu.cwm.databinding.PreviewBinding
import me.qingshu.cwm.screen.adapter.ArtSignatureItem
import me.qingshu.cwm.screen.adapter.CardColorCheckableItem
import me.qingshu.cwm.screen.adapter.CardGravityItem
import me.qingshu.cwm.screen.adapter.CardIconItem
import me.qingshu.cwm.screen.adapter.CardSizeItem
import me.qingshu.cwm.screen.adapter.CardStylesItem
import me.qingshu.cwm.screen.adapter.EffectItem
import me.qingshu.cwm.screen.adapter.MainMenuItem
import me.qingshu.cwm.screen.adapter.TemplateItem
import me.qingshu.cwm.style.Styles
import me.qingshu.cwm.style.card.CardStyleBuilder
import me.qingshu.cwm.style.def.DefaultStyleBuilder
import me.qingshu.cwm.style.inner.CardInnerBuilder
import me.qingshu.cwm.style.space.SpaceStyleBuilder

class MainViewModel : ViewModel() {

    private val style = MutableStateFlow(Styles.DEFAULT)
    val styles = combine(
        style,
        MutableStateFlow(Styles.values()
        )
    ){ currentStyle,array ->
        array.map {
            CardStylesItem(currentStyle==it,it)
        }
    }
    fun receiveStyle(s: Styles){
        viewModelScope.launch {
            style.emit(s)
        }
    }

    val saveEnable = MutableStateFlow(true)

    val mainMenu = combine(
        saveEnable,
        MutableStateFlow(
            CardMenu.values()
        )
    ){ save,array ->
        array.map { cardMenu ->
            MainMenuItem(save,cardMenu)
        }
    }

    private val template = MutableStateFlow(Template.empty)
    private val templates = MutableStateFlow(emptyList<Template>())

    val templateItems = combine(template,templates){ t,ts ->
        ts.map {
            TemplateItem(t.key == it.key,it)
        }
    }
    fun receiveTemplate(array: List<Template>, currentTemplate:Long = 0){
        viewModelScope.launch {
            templates.emit(array)
            if(currentTemplate>0){
                array.find {
                    it.key == currentTemplate
                }?.also {
                    loadTemplate(it,fromUser = true)
                }
            }
        }
    }
    fun modifierTemplate(newName:String,t:Template,callback:(List<Template>)->Unit){
        viewModelScope.launch {
            val target = templates.value.toMutableList()
            val index = target.indexOf(t)
            target.removeAt(index)
            val newTemplate = getNewTemplate(newName,t.key)
            target.add(index,newTemplate)
            templates.emit(target)
            callback.invoke(target)
        }
    }
    fun  deleteTemplate(t:Template,callback:(List<Template>)->Unit){
        viewModelScope.launch {
            val target = templates.value.toMutableList()
            target.remove(t)
            templates.emit(target)
            callback.invoke(target)
        }
    }
    private fun getNewTemplate(templateName: String,key:Long):Template{
        return Template(
            device = Device(brand = deviceBrand.value, model = deviceModel.value),
            information = Information(date = infoDate.value, location = infoLocation.value),
            lens = Lens(
                param = lensParam.value,
                focalDistance = lensFocalDistance.value,
                iso = lensIso.value,
                shutter = lensShutter.value,
                aperture = lensAperture.value,
                paramVisible = lensParamVisible.value
            ),
            cardColor = color.value,
            cardSize = size.value,
            logo = logo.value,
            artSignature = ArtSignature(artSignatureText.value,artSignatureVisible.value),
            name = templateName,
            key = key,
            style = style.value
        )
    }
    fun saveTemplate(templateName:String = "",saveCall:(List<Template>,Long)->Unit){
        viewModelScope.launch {
            val key = System.currentTimeMillis()
            getNewTemplate(templateName,key).also { t ->
                val target = templates.value.toMutableList()
                target.add(t)
                templates.emit(target)
                template.emit(t)
                saveCall.invoke(target,key)
            }
        }
    }
    fun loadTemplate(t: Template,fromUser:Boolean = false){
        viewModelScope.launch {
            template.emit(t)
            t.device.also {
                deviceBrand.emit(it.brand)
                deviceModel.emit(it.model)
            }
            t.information.also {
                infoDate.emit(it.date)
                infoLocation.emit(it.location)
            }
            t.lens.also {
                lensFocalDistance.emit(it.focalDistance)
                lensAperture.emit(it.aperture)
                lensIso.emit(it.iso)
                lensParam.emit(it.param)
                lensShutter.emit(it.shutter)
                lensParamVisible.emit(it.paramVisible)
            }
            t.logo.also { receiveLogo(it) }
            t.artSignature.also {
                artSignatureText.emit(it.text)
                artSignatureVisible.emit(it.visible)
            }
            t.cardColor.also { color.emit(it) }
            t.cardSize.also { size.emit(it) }
            t.style.also { style.emit(it) }
            if(fromUser) sendExif()
        }
    }

    private val effect = MutableStateFlow(emptySet<CardEffect>())
    fun receiveEffect(cardEffect: CardEffect){
        viewModelScope.launch {
            val target = effect.value.toMutableSet()
            if(target.contains(cardEffect)) target-=cardEffect
            else target+=cardEffect
            effect.emit(target)
        }
    }
    val effects = combine(
        effect,
        MutableStateFlow(
            CardEffect.values()
                .toMutableList()
                .subList(1,CardEffect.values().size)
        ),
    ){ currentEffect,array ->
        array.map {
            EffectItem(it in currentEffect,it)
        }
    }

    private val color = MutableStateFlow(CardColor.WHITE)
    val colors = combine(
        color,
        MutableStateFlow(CardColor.values())
    ){ currentColor,array ->
        array.map {
            CardColorCheckableItem(currentColor==it,it)
        }
    }
    fun receiveColor(c: CardColor){
        viewModelScope.launch {
            color.emit(c)
        }
    }

    private val size = MutableStateFlow(CardSize.LARGE)
    val sizes = combine(
        size,
        MutableStateFlow(CardSize.values())
    ){
        currentSize,array ->
        array.map{
            CardSizeItem(currentSize==it,it)
        }
    }
    fun receiveSize(s:CardSize){
        viewModelScope.launch {
            size.emit(s)
        }
    }

    private val gravity = MutableStateFlow(emptySet<CardGravity>())
    val gravityItems = combine(
        gravity,
        MutableStateFlow(
            CardGravity.values()
                .toMutableList()
                .subList(1,CardGravity.values().size)
        )
    ){ currentGravity,array ->
        array.map {
            CardGravityItem(it in currentGravity,it)
        }
    }
    fun receiveGravity(cardGravity: CardGravity){
        viewModelScope.launch {
            val target = gravity.value.toMutableSet()
            if(target.contains(cardGravity)) target-=cardGravity
            else target+=cardGravity
            gravity.emit(target)
        }
    }

    private val _message = Channel<Int>(capacity = Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    private val _picker = Channel<Unit>(capacity = Channel.CONFLATED)
    val picker = _picker.receiveAsFlow()
    fun picker(){
        _picker.trySend(Unit)
    }

    private val _save = Channel<Unit>(capacity = Channel.CONFLATED)
    val save = _save.receiveAsFlow()
    fun save(){
        _save.trySend(Unit)
    }

    private val pictureUris = MutableStateFlow<List<UriExif>>(emptyList())

    //device
    val deviceBrand = MutableStateFlow("")
    val deviceModel = MutableStateFlow("")

    //lens
    val lensParam = MutableStateFlow("")
    val lensFocalDistance = MutableStateFlow("")
    val lensAperture = MutableStateFlow("")
    val lensShutter = MutableStateFlow("")
    val lensIso = MutableStateFlow("")
    val lensParamVisible = MutableStateFlow(false)

    //information
    val infoDate = MutableStateFlow("")
    val infoLocation = MutableStateFlow("")

    //wordArt
    val artSignatureText = MutableStateFlow("")
    val artSignatureVisible = MutableStateFlow(false)
    fun toggleArtSignatureVisible(){
        viewModelScope.launch {
            artSignatureVisible.emit(!artSignatureVisible.value)
        }
    }
    val artSignatureFont = MutableStateFlow(Font.QuillbacksDemo)
    fun receiveFont(font: Font){
        viewModelScope.launch {
            artSignatureFont.emit(font)
        }
    }
    val artSignatureItem = combine(
        artSignatureFont,
        MutableStateFlow(
            Font.values()
        )
    ){ currentFont,array ->
        array.map {
            ArtSignatureItem(currentFont==it,it)
        }
    }

    private val userExif = MutableStateFlow(Exif.empty())

    val artSignature = combine(artSignatureText,artSignatureVisible,artSignatureFont){ text, visible,font ->
        ArtSignature(text,visible,font)
    }
    fun receiveArtSignature(word: ArtSignature){
        viewModelScope.launch {
            artSignatureText.emit(word.text)
            artSignatureVisible.emit(word.visible)
        }
    }

    fun sendExif(){
        viewModelScope.launch {
            Exif(
                Device(deviceBrand.value, deviceModel.value),
                Lens(lensParam.value, lensFocalDistance.value, lensAperture.value, lensShutter.value, lensIso.value, lensParamVisible.value),
                Information(infoDate.value, infoLocation.value),
                ArtSignature(artSignatureText.value,artSignatureVisible.value)
            ).also {
                userExif.emit(it)
            }
        }
    }

    private val _pictureExif = Channel<ExifInterface>(capacity = Channel.CONFLATED)
    val pictureExif = _pictureExif.receiveAsFlow()
    fun exif(picture: Picture, context: Context){
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

    private val logo = MutableStateFlow(Logo.CANNON)
    fun receiveLogo(icon: Logo) {
        viewModelScope.launch {
            logo.emit(icon)
        }
    }


    private val logoVisible = MutableStateFlow(true)
    fun receiveLogoVisible(visible:Boolean){
        viewModelScope.launch {
            logoVisible.emit(visible)
        }
    }
    val cardIconItem = combine(
        logoVisible,
        MutableStateFlow(CardIcon.values())
    ){ visible,array ->
        array.mapIndexed { index, icon ->
            CardIconItem(visible && index==0,icon)
        }
    }

    val previewPictures = combine(
        pictureUris, color, size,
        logo, userExif,style,
        gravity,effect,logoVisible,artSignature
    ) { any ->
        val uris = any[0] as List<UriExif>
        val cardColor = any[1] as CardColor
        val cardSize = any[2] as CardSize
        val icon = any[3] as Logo
        val exif = any[4] as Exif
        val style = any[5] as Styles
        val gravity = any[6] as Set<CardGravity>
        val f = any[7] as Set<CardEffect>
        val visible = any[8] as Boolean
        val art = any[9] as ArtSignature
        saveEnable.emit(false)
        uris.map { ue ->
            val targetDevice = Device.combine(exif.device,ue.exif.device)
            val targetLens = Lens.combine(exif.lens,ue.exif.lens)
            val targetInfo = Information.combine(exif.information,ue.exif.information)
            Picture(
                ue.uri, cardSize, cardColor,
                icon, Exif(targetDevice, targetLens, targetInfo,art),
                styles = style,
                gravity = CardGravity.value(gravity),
                effect = CardEffect.value(f),
                artSignature = art,
                visibleIcon = visible
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

    fun save(context: Context, binding: PreviewBinding) {
        val target = when(style.value){
            Styles.DEFAULT -> DefaultStyleBuilder(binding.styleDefault)
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