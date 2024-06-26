package me.qingshu.cwm.style.space

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleSpaceBinding
import me.qingshu.cwm.style.StyleMarkBinding

class SpaceMarkBinding(
    binding: StyleSpaceBinding
) : StyleMarkBinding<StyleSpaceBinding>(binding) {

    val exifRoot get() = binding.exifRoot
    val src get() = binding.src
    val card get() = binding.card

    fun realHeight(bitmap: Bitmap, picture: Picture) =
        picture.cardSize.sizeByHeight(bitmap.height, bitmap.width)

    fun color(@ColorRes resValue: Int) = ContextCompat.getColor(src.context, resValue)

    override fun clear(): SpaceMarkBinding {
        binding.apply {
            logo.setImageDrawable(null)
            shutter.text = emptyString
            iso.text = emptyString
            aperture.text = emptyString
            artSignature.text = emptyString
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
        if (height * width == 0) return@apply
        clear()
        val isPreview = click != null
        val size = picture.cardSize.logoSizeByHeight(height, width)
        val textColor = getTextColor(picture)
        val bgColor = getBgColor(picture)
        val exifHeight = picture.cardSize.sizeByHeight(height, width)
        val ts = width / 100
        bg.setOnClickListener {
            click?.invoke(it, picture)
        }

        if (picture.isBlur()) picture.blur?.also {
            bg.background = BitmapDrawable(context.resources, it)
        }
        else bg.setBackgroundColor(bgColor)

        exifRoot.apply {
            layoutParams.width = width / 10 * 4
            if (!isPreview) {
                layoutParams.height = height
            }
            if (!picture.isBlur()) setBackgroundColor(bgColor)
            else setBackgroundColor(0x0)
        }

        deviceRoot.setPadding(ts * 3, 0, 0, 0)
        space.layoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            ts * (if (isPreview) 1 else 4)
        )



        if (picture.artSignature.visible) artSignature.text = picture.artSignature.text
        else logo.apply {
            setImageResource(picture.icon.src)
            layoutParams.also {
                it.width = (width / 10f * 4f / 2.5f).toInt()
                it.height = (exifHeight / 10f * 9f).toInt()
            }
            setPadding(picture.icon.padding.dp / 2)
        }

        if(!picture.artSignature.visible){
            val v = if(picture.visibleIcon) View.VISIBLE else View.GONE
            logo.visibility = v
            space.visibility = v
        }else {
            space.visibility = View.VISIBLE
            logo.visibility = View.GONE
        }
        artSignature.visibility = if (picture.artSignature.visible) View.VISIBLE else View.GONE

        shutter.text = picture.userExif.lens.shutter
        iso.text = picture.userExif.lens.iso
        aperture.text = picture.userExif.lens.aperture
        focalDistance.text = picture.userExif.lens.focalDistance
        val base = if (!isPreview) 1 else 1 / 2

        arrayOf(aperture, iso, shutter, focalDistance).forEach {
            it.visibility = if (it.text.trim().isEmpty()) View.GONE else View.VISIBLE
            it.textSize = ts.toFloat() / 10f * 9f
            it.textColor = textColor
            it.typeface = typeface()
            it.setPadding(ts * base)
            it.shadow(ts.toFloat(),textColor,picture)
        }
        if (picture.icon.tintEnable) logo.setColorFilter(textColor)
        else logo.colorFilter = null

        artSignature.typeface = picture.artSignature.typeface(context)
        artSignature.setTextColor(textColor)
        artSignature.textSize = ts * 1.6f
    }
}