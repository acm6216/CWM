package me.qingshu.cwm.style.def

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.style.StyleMarkBinding

class DefaultMarkBinding(
    styleDefaultBinding: StyleDefaultBinding
): StyleMarkBinding<StyleDefaultBinding>(styleDefaultBinding) {

    val exifRoot get() = binding.exifRoot

    val src get() = binding.src

    fun realHeight(bitmap: Bitmap,picture: Picture) = picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)

    override fun clear(): DefaultMarkBinding {
        binding.apply {
            logo.setImageDrawable(null)
            location.text = emptyString
            device.text = emptyString
            date.text = emptyString
            lens.text = emptyString
            artSignature.text = emptyString
            root.invalidate()
        }
        return this
    }

    override fun setMark(
        picture: Picture,
        height:Int,
        width:Int,
        click: ((View,Picture) -> Unit)?
    ) = binding.apply {
        if(height*width==0) return@apply
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val textColor = getTextColor(picture)
        val bgColor = getBgColor(picture)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        val ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        bg.setOnClickListener{
            click?.invoke(it,picture)
        }

        if(picture.isBlur()) picture.blur?.also {
            bg.background = BitmapDrawable(context.resources,it)
        }
        else bg.setBackgroundColor(bgColor)

        exifRoot.apply {
            layoutParams.height = exifHeight
            if(!picture.isBlur()) setBackgroundColor(bgColor)
            else setBackgroundColor(0x0)
        }
        infoRoot.setPadding(0,0,exifHeight/4,0)
        deviceRoot.setPadding(exifHeight/4,0,0,0)

        if(picture.artSignature.visible) artSignature.text = picture.artSignature.text
        else logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = size * 2
                it.height = size
            }
            setPadding(picture.icon.iconPadding().dp)
        }

        if(!picture.artSignature.visible){
            val v = if(picture.visibleIcon) View.VISIBLE else View.GONE
            logo.visibility = v
            dividerRoot.visibility = v
        }else {
            dividerRoot.visibility = View.VISIBLE
            logo.visibility = View.GONE
        }
        artSignature.visibility = if(picture.artSignature.visible) View.VISIBLE else View.GONE

        device.text = picture.userExif.device.string()
        lens.text = picture.userExif.lens.string()

        date.text = picture.userExif.information.date.trimStart()
        location.text = picture.userExif.information.location
        dividerRoot.layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)*9
        divider.apply {
            Color.argb(
                128,
                textColor.red,
                textColor.green,
                textColor.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height = picture.cardSize.dividerHeightSizeByHeight(height, width)
            layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)
        }
        if (picture.icon.tintEnable) logo.setColorFilter(textColor)
        else logo.colorFilter = null
        arrayOf(device,lens).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColor)
            it.typeface = typeface()
            it.shadow(ts,textColor,picture)
        }
        arrayOf(date,location).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts/1.5f
            it.typeface = typeface()
            it.setTextColor(Color.argb(128,textColor.red,textColor.green,textColor.blue))
            it.shadow(ts,textColor,picture)
        }
        if(date.visibility==View.GONE){
            device.textSize = ts*1.4f
        }
        artSignature.typeface = picture.artSignature.typeface(context)
        artSignature.setTextColor(textColor)
        artSignature.textSize = ts*1.6f
        artSignature.shadow(ts,textColor,picture)
    }

}