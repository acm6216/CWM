package me.qingshu.cwm.binding

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PictureItemBinding

class PictureMarkBinding(private val pictureItemBinding: PictureItemBinding) {

    private val binding get() = pictureItemBinding
    private val context get() = binding.root.context
    val exifRoot get() = binding.exifRoot

    val src get() = binding.src

    val root get() = pictureItemBinding.root

    fun height(picture: Picture) = picture.cardSize.size.dp

    fun realHeight(bitmap: Bitmap,picture: Picture) = picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)

    fun clear():PictureMarkBinding {
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

    fun setMark(
        picture: Picture,
        height:Int,
        width:Int,
        click: ((View,Picture) -> Unit)? = null
    ) = binding.apply {
        if(height*width==0) return@apply
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor())
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        val ts = picture.cardSize.textSizeByHeight(height, width).toFloat()
        card.setOnClickListener{
            click?.invoke(it,picture)
        }
        exifRoot.apply {
            layoutParams.height = exifHeight
            setBackgroundResource(picture.cardColor.color)
        }
        infoRoot.setPadding(0,0,exifHeight/4,0)
        deviceRoot.setPadding(exifHeight/4,0,0,0)
        logo.apply {
            setImageResource(picture.logo.src)
            layoutParams.also {
                it.width = size * 2
                it.height = size
            }
            setPadding(picture.logo.iconPadding().dp)
        }
        device.text = picture.userExif.device.string()
        lens.text = picture.userExif.lens.string()
        date.text = picture.userExif.information.date.trimStart()
        location.text = picture.userExif.information.location
        dividerRoot.layoutParams.width = picture.cardSize.divideWidthSizeByHeight(height, width)*9
        divider.apply {
            Color.argb(
                128,
                textColorValue.red,
                textColorValue.green,
                textColorValue.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height = picture.cardSize.dividerHeightSizeByHeight(height, width)
            layoutParams.width = picture.cardSize.divideWidthSizeByHeight(height, width)
        }
        if (picture.logo.tintEnable) logo.setColorFilter(textColorValue)
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

    private inline val Int.dp: Int get() = run { toFloat().dp }
    private inline val Float.dp: Int get() = run {
        val scale: Float = root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

}