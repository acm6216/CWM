package me.qingshu.cwm.style.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleCardBinding
import me.qingshu.cwm.style.CORNER_ALL
import me.qingshu.cwm.style.StyleBuilder
import me.qingshu.cwm.style.radius
import me.qingshu.cwm.style.shadow

class CardStyleBuilder(
    private val binding: StyleCardBinding
): StyleBuilder<StyleCardBinding>(binding) {

    override fun execute(
        context: Context,
        picture: Picture,
        coroutineScope: CoroutineScope
    ) {
        val layout = CardMarkBinding(binding)

        coroutineScope.launch(Dispatchers.Main) { layout.clear() }

        val sourceBitmap = context.contentResolver.openInputStream(picture.uri)!!.use { stream ->
            val source = BitmapFactory.decodeStream(stream,
                Rect(0, 0, 0, 0), BitmapFactory.Options().apply
                {

                })
            source!!.let {
                val isRadius = picture.isCorner()
                if(isRadius) it.radius(source.width / 20f.toInt(), CORNER_ALL)
                else it
            }
        }

        val size = layout.realHeight(sourceBitmap, picture)
        layout.exifRoot.layoutParams.apply {
            height = size
            width = sourceBitmap.width
        }
        coroutineScope.launch(Dispatchers.Main) {
            layout.setMark(
                picture = picture,
                height = sourceBitmap.height,
                width = sourceBitmap.width
            )
        }
        Thread.sleep(250)
        val bitmap = Bitmap.createBitmap(
            sourceBitmap.width,
            size,
            Bitmap.Config.ARGB_8888
        )
        layout.exifRoot.draw(Canvas(bitmap).apply {
            density = sourceBitmap.density
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        })

        val margin = sourceBitmap.width/16f
        val shadowSize = sourceBitmap.width/20f

        val newBitmap = Bitmap.createBitmap(
            sourceBitmap.width+(margin*2).toInt(),
            sourceBitmap.height + bitmap.height+margin.toInt()+shadowSize.toInt(),
            Bitmap.Config.ARGB_8888
        )
        val textColorValue = ContextCompat.getColor(context,picture.cardColor.textColor)
        val isRadius = picture.isCorner()
        val isShadow = picture.isShadowPicture()
        Canvas(newBitmap).apply {
            drawColor(layout.color(picture.cardColor.bgColor))
            density = sourceBitmap.density
            drawBitmap(sourceBitmap.shadow(shadowSize,textColorValue,isRadius,isShadow), margin-shadowSize, margin-shadowSize, null)
            drawBitmap(bitmap, margin, margin + sourceBitmap.height.toFloat()+shadowSize, null)
        }
        saveBitmap(context, picture, newBitmap)
    }

}