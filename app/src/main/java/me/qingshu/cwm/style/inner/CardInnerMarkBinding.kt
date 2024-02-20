package me.qingshu.cwm.style.inner

import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleCardInnerBinding
import me.qingshu.cwm.style.StyleMarkBinding

class CardInnerMarkBinding(
    styleCardBinding: StyleCardInnerBinding
): StyleMarkBinding<StyleCardInnerBinding>(styleCardBinding) {

    val src get() = binding.src
    val exifRoot get() = binding.exifRoot

    fun color(@ColorRes resValue:Int) = ContextCompat.getColor(src.context,resValue)

    override fun clear(): StyleMarkBinding<StyleCardInnerBinding> {
        binding.apply {
            logo.setImageDrawable(null)
            device.text = ""
            lens.text = ""
            exifRoot.gravity = Gravity.START
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
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.bgColor)
        val ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val hv = picture.cardSize.dividerHeightSizeByHeight(height, width)/2
        logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = size * 2
                it.height = size
            }
            setPadding(picture.icon.iconPadding().dp)
            if (picture.icon.tintEnable) setColorFilter(textColorValue)
            else colorFilter = null
            visibility = if(picture.visible) View.VISIBLE else View.GONE
        }
        cardRoot.setOnClickListener{
            click?.invoke(it,picture)
        }
        exifRoot.setPadding(hv*2)
        exifRoot.gravity = picture.gravity.flags
        lens.text = picture.userExif.lens.string()
        device.text = picture.userExif.device.string()
        device.setPadding(0,0,0,hv/8)
        lens.setPadding(0)

        arrayOf(device,lens).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColorValue)
        }

        cardRoot.setBackgroundResource(picture.cardColor.bgColor)
    }
}