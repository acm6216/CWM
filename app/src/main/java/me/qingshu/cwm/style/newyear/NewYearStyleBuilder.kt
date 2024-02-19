package me.qingshu.cwm.style.newyear

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
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.style.StyleBuilder

class NewYearStyleBuilder(
    private val binding: StyleDefaultBinding
) : StyleBuilder<StyleDefaultBinding>(binding) {

    override fun execute(
        context: Context,
        picture: Picture,
        coroutineScope: CoroutineScope
    ) {
        val layout = NewYearMarkBinding(binding)

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
            drawBitmap(sourceBitmap, 0f, 0f, null)
            drawBitmap(bitmap, 0f, sourceBitmap.height.toFloat(), null)
        }
        saveBitmap(context, picture, newBitmap)
    }
}