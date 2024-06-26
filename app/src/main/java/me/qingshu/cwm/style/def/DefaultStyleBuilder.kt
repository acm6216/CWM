package me.qingshu.cwm.style.def

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.style.StyleBuilder

class DefaultStyleBuilder(
    private val binding: StyleDefaultBinding
) : StyleBuilder<StyleDefaultBinding>(binding) {

    override fun execute(
        context: Context,
        picture: Picture,
        coroutineScope: CoroutineScope
    ) {
        val layout = DefaultMarkBinding(binding)

        coroutineScope.launch(Dispatchers.Main) { layout.clear() }

        val sourceBitmap = context.contentResolver.openInputStream(picture.uri)!!.use { stream ->
            BitmapFactory.decodeStream(stream)!!
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

        val newBitmap = Bitmap.createBitmap(
            sourceBitmap.width,
            sourceBitmap.height + bitmap.height,
            Bitmap.Config.ARGB_8888
        )
        Canvas(newBitmap).apply {
            density = sourceBitmap.density
            blur(picture,sourceBitmap,newBitmap)
            drawBitmap(sourceBitmap, 0f, 0f, null)
            drawBitmap(bitmap, 0f, sourceBitmap.height.toFloat(), null)
        }
        saveBitmap(context, picture, newBitmap)
    }
}