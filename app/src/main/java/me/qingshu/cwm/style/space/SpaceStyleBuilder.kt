package me.qingshu.cwm.style.space

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleSpaceBinding
import me.qingshu.cwm.style.StyleBuilder

class SpaceStyleBuilder(
    private val binding:StyleSpaceBinding
): StyleBuilder<StyleSpaceBinding>(binding) {
    override fun execute(context: Context, picture: Picture, coroutineScope: CoroutineScope) {

        val layout = SpaceMarkBinding(binding)

        coroutineScope.launch(Dispatchers.Main) { layout.clear() }

        val sourceBitmap = context.contentResolver.openInputStream(picture.uri)!!.use { stream ->
            val source = BitmapFactory.decodeStream(stream,
                Rect(0, 0, 0, 0), BitmapFactory.Options().apply
                {

                })
            source!!
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
        val exifW = sourceBitmap.width/10*4
        val bitmap = Bitmap.createBitmap(
            exifW,
            sourceBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        layout.exifRoot.draw(Canvas(bitmap).apply {
            density = sourceBitmap.density
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        })

        val margin = sourceBitmap.width/15f
        val shadowSize = sourceBitmap.width/20f

        val newBitmap = Bitmap.createBitmap(
            sourceBitmap.width+(margin*2).toInt() + exifW,
            sourceBitmap.height + (margin*2).toInt(),
            Bitmap.Config.ARGB_8888
        )
        Canvas(newBitmap).apply {
            drawColor(layout.color(picture.cardColor.bgColor))
            density = sourceBitmap.density
            drawBitmap(sourceBitmap, margin, margin, null)
            drawBitmap(bitmap, margin+sourceBitmap.width.toFloat(), margin, null)
        }
        saveBitmap(context, picture, newBitmap)
    }
}