package me.qingshu.cwm.style.newyear

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.style.StyleMarkBinding

class NewYearMarkBinding(
    styleDefaultBinding: StyleDefaultBinding
): StyleMarkBinding<StyleDefaultBinding>(styleDefaultBinding) {

    val exifRoot get() = binding.exifRoot

    val src get() = binding.src

    fun realHeight(bitmap: Bitmap, picture: Picture) = picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)

    override fun clear(): NewYearMarkBinding {
        binding.apply {
            logo.setImageDrawable(null)
            location.text = ""
            device.text = ""
            date.text = ""
            lens.text = ""
            root.invalidate()
        }
        return this
    }

    override fun setMark(
        picture: Picture,
        height:Int,
        width:Int,
        click: ((View, Picture) -> Unit)?
    ) = binding.apply {
        if(height*width==0) return@apply
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        val ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        card.setOnClickListener{
            click?.invoke(it,picture)
        }
        exifRoot.apply {
            layoutParams.height = exifHeight
            setBackgroundResource(picture.cardColor.bgColor)
        }
        infoRoot.setPadding(0,0,exifHeight/4,0)
        deviceRoot.setPadding(exifHeight/4,0,0,0)
        logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = (size * 2.2f).toInt()
                it.height = exifHeight
            }
            setPadding(picture.icon.padding.dp)
        }
        device.text = picture.userExif.device.string()
        lens.text = picture.userExif.information.location
        date.text = picture.userExif.lens.string()
        location.text = picture.userExif.information.date
        dividerRoot.layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)*9
        divider.apply {
            Color.argb(
                128,
                textColorValue.red,
                textColorValue.green,
                textColorValue.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height = picture.cardSize.dividerHeightSizeByHeight(height, width)
            layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)
        }
        if (picture.icon.tintEnable) logo.setColorFilter(textColorValue)
        else logo.colorFilter = null
        arrayOf(device,lens).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColorValue)
        }
        arrayOf(date,location).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts/1.5f
            it.setTextColor(Color.argb(128,textColorValue.red,textColorValue.green,textColorValue.blue))
        }

    }

}