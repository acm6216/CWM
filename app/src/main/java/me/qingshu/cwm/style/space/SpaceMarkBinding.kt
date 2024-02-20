package me.qingshu.cwm.style.space

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleSpaceBinding
import me.qingshu.cwm.style.StyleMarkBinding

class SpaceMarkBinding(
    binding:StyleSpaceBinding
): StyleMarkBinding<StyleSpaceBinding>(binding) {

    val exifRoot get() = binding.exifRoot
    val src get() = binding.src
    val card get() = binding.card

    fun realHeight(bitmap: Bitmap, picture: Picture) = picture.cardSize.sizeByHeight(bitmap.height,bitmap.width)

    fun color(@ColorRes resValue:Int) = ContextCompat.getColor(src.context,resValue)

    override fun clear(): SpaceMarkBinding {
        binding.apply {
            logo.setImageDrawable(null)
            shutter.text = ""
            iso.text = ""
            aperture.text = ""
            root.invalidate()
        }
        return this
    }

    override fun setMark(
        picture: Picture,
        height: Int,
        width: Int,
        click: ((View, Picture) -> Unit)?
    ) = binding.apply {
        if(height*width==0) return@apply
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        val ts = width/100
        card.setOnClickListener{
            click?.invoke(it,picture)
        }
        cardRoot.setBackgroundResource(picture.cardColor.bgColor)
        exifRoot.apply {
            layoutParams.width = width/10*4
            if(click==null) {
                layoutParams.height = height
            }
            setBackgroundResource(picture.cardColor.bgColor)
        }
        //infoRoot.setPadding(0,0,exifHeight/4,0)
        deviceRoot.setPadding(ts*3,0,0,0)
        logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = (width/10f*4f/2.5f).toInt()
                it.height = (exifHeight/10f*9f).toInt()
            }
            setPadding(picture.icon.padding.dp/2)
        }
        shutter.text = picture.userExif.lens.shutter
        iso.text = picture.userExif.lens.iso
        aperture.text = picture.userExif.lens.aperture
        focalDistance.text = picture.userExif.lens.focalDistance
        val base = if(click==null) 2 else 1/4

        arrayOf(aperture,iso,shutter,focalDistance).forEach {
            it.visibility = if(it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts.toFloat()/10f*9f
            it.setTextColor(textColorValue)
            it.isPreview = click!=null
            it.setPadding(0,ts*base,0,ts*base)
        }
        if (picture.icon.tintEnable) logo.setColorFilter(textColorValue)
        else logo.colorFilter = null

    }
}