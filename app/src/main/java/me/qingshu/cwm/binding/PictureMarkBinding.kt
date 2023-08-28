package me.qingshu.cwm.binding

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import coil.load
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PictureItemBinding

class PictureMarkBinding(private val pictureItemBinding: PictureItemBinding) {

    private val binding get() = pictureItemBinding
    private val context get() = binding.root.context
    val exifRoot get() = binding.exifRoot

    val root get() = pictureItemBinding.root

    fun height(picture: Picture) = picture.cardSize.size.dp

    fun realHeight(bitmap: Bitmap,picture: Picture) = picture.cardSize.sizeByBitmap(bitmap)

    fun clear():PictureMarkBinding{
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
        isPreview:Boolean = true,
        bitmap: Bitmap?=null,
        click: ((View,Picture) -> Unit)? = null
    ) = binding.apply {
        val size = if(isPreview) picture.cardSize.logoSize.dp else picture.cardSize.logoSizeByBitmap(bitmap!!)
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor())
        val exifHeight = if(isPreview) picture.cardSize.size.dp else picture.cardSize.sizeByBitmap(bitmap!!)
        val ts = if(isPreview) picture.cardSize.textSize else picture.cardSize.textSizeByBitmap(bitmap!!).toFloat()
       card.setOnClickListener{
           click?.invoke(it,picture)
       }
        if (isPreview)
            src.load(picture.uri) {
                crossfade(true)
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
        dividerRoot.apply {
            if(!isPreview) layoutParams.width = picture.cardSize.divideWidthSizeByBitmap(bitmap!!)*9
        }
        divider.apply {
            Color.argb(
                128,
                textColorValue.red,
                textColorValue.green,
                textColorValue.blue
            ).also { setBackgroundColor(it) }
            layoutParams.height =
                if (isPreview) (size / 2f).toInt() else picture.cardSize.dividerHeightSizeByBitmap(
                    bitmap!!
                )
            if(!isPreview) {
                layoutParams.width = picture.cardSize.divideWidthSizeByBitmap(
                    bitmap!!
                )
            }
        }
        if (picture.logo.tintEnable) logo.setColorFilter(textColorValue)
        else logo.colorFilter = null
        arrayOf(device,lens,date,location).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts
            it.setTextColor(textColorValue)
        }
        date.textSize = ts / 1.5f
        location.textSize = ts / 1.5f
    }

    private inline val Int.dp: Int get() = run { toFloat().dp }
    private inline val Float.dp: Int get() = run {
        val scale: Float = root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

}