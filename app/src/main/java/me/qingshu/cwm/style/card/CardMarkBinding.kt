package me.qingshu.cwm.style.card

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
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
    val exifRoot get() = binding.exifRoot

    fun color(@ColorRes resValue:Int) = ContextCompat.getColor(src.context,resValue)

    fun realHeight(bitmap: Bitmap, picture: Picture) = (picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)*1.4f).toInt()

    override fun clear(): StyleMarkBinding<StyleCardBinding> {
        binding.apply {
            logo.setImageDrawable(null)
            device.text = emptyString
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
        click: ((View, Picture) -> Unit)?
    ):Any = binding.apply {
        val textColor = getTextColor(picture)
        val bgColor = getBgColor(picture)
        var ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)

        if(picture.artSignature.visible) artSignature.text = picture.artSignature.text
        else logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = size * 2
                it.height = size
            }
            setPadding(picture.icon.iconPadding().dp)
            if (picture.icon.tintEnable) setColorFilter(textColor)
            else colorFilter = null
        }

        if(!picture.artSignature.visible){
            val v = if(picture.visibleIcon) View.VISIBLE else {
                ts*=1.5f
                View.GONE
            }
            logo.visibility = v
        }else {
            divider.visibility = View.VISIBLE
            logo.visibility = View.GONE
        }
        artSignature.visibility = if(picture.artSignature.visible) View.VISIBLE else View.GONE

        divider.apply {
            Color.argb(
                128,
                textColor.red,
                textColor.green,
                textColor.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height = picture.cardSize.dividerHeightSizeByHeight(height, width)/2
            layoutParams.width = picture.cardSize.dividerWidthSizeByHeight(height, width)
        }
        cardRoot.setOnClickListener{
            click?.invoke(it,picture)
        }
        exifRoot.apply {
            layoutParams.height = (exifHeight*1.4f).toInt()
            if(!picture.isBlur()) setBackgroundColor(bgColor)
            else setBackgroundColor(0x0)
        }
        lens.text = picture.userExif.lens.string()
        device.text = picture.userExif.device.string()
        val hv = picture.cardSize.dividerHeightSizeByHeight(height, width)/2
        device.setPadding(0,0,hv,0)
        lens.setPadding(hv,0,0,0)

        arrayOf(device,lens).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColor)
            it.typeface = typeface()
            it.shadow(ts,textColor,picture)
        }
        artSignature.typeface = picture.artSignature.typeface(context)
        artSignature.textSize = ts*1.6f
        artSignature.setTextColor(textColor)
        artSignature.shadow(ts,textColor,picture)

        if(picture.isBlur()) picture.blur?.also {
            bg.background = BitmapDrawable(context.resources,it)
        }
        else bg.setBackgroundColor(bgColor)
    }

}