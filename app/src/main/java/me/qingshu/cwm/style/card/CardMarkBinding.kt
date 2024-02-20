package me.qingshu.cwm.style.card

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleCardBinding
import me.qingshu.cwm.style.StyleMarkBinding

class CardMarkBinding(
    styleCardBinding: StyleCardBinding
): StyleMarkBinding<StyleCardBinding>(styleCardBinding) {

    val src get() = binding.src
    val card get() = binding.card
    val cardRoot get() = binding.cardRoot
    val exifRoot get() = binding.exifRoot

    fun color(@ColorRes resValue:Int) = ContextCompat.getColor(src.context,resValue)

    fun realHeight(bitmap: Bitmap, picture: Picture) = (picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)*1.4f).toInt()

    override fun clear(): StyleMarkBinding<StyleCardBinding> {
        binding.apply {
            logo.setImageDrawable(null)
            device.text = ""
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
    ):Any = binding.apply {
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor)
        val ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = size * 2
                it.height = size
            }
            setPadding(picture.icon.iconPadding().dp)
            if (picture.icon.tintEnable) setColorFilter(textColorValue)
            else colorFilter = null
        }
        divider.apply {
            Color.argb(
                128,
                textColorValue.red,
                textColorValue.green,
                textColorValue.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height = picture.cardSize.dividerHeightSizeByHeight(height, width)/2
            layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)
        }
        cardRoot.setOnClickListener{
            click?.invoke(it,picture)
        }
        exifRoot.apply {
            layoutParams.height = (exifHeight*1.4f).toInt()
            setBackgroundResource(picture.cardColor.bgColor)
        }
        lens.text = picture.userExif.lens.string()
        device.text = picture.userExif.device.string()
        val hv = picture.cardSize.dividerHeightSizeByHeight(height, width)/2
        device.setPadding(0,0,hv,0)
        lens.setPadding(hv,0,0,0)

        arrayOf(device,lens).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColorValue)
        }

        cardRoot.setBackgroundResource(picture.cardColor.bgColor)
    }

}