package me.qingshu.cwm.style.inner

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
import me.qingshu.cwm.databinding.StyleCardInnerBinding
import me.qingshu.cwm.style.StyleBuilder

class CardInnerBuilder(
    private val binding: StyleCardInnerBinding
) : StyleBuilder<StyleCardInnerBinding>(binding) {
    override fun execute(context: Context, picture: Picture, coroutineScope: CoroutineScope) {

        val layout = CardInnerMarkBinding(binding)

        coroutineScope.launch(Dispatchers.Main) { layout.clear() }

        val sourceBitmap = context.contentResolver.openInputStream(picture.uri)!!.use { stream ->
            BitmapFactory.decodeStream(stream)!!
        }

        layout.exifRoot.layoutParams.apply {
            height = sourceBitmap.height
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
            sourceBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        layout.exifRoot.draw(Canvas(bitmap).apply {
            density = sourceBitmap.density
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        })

        val newBitmap = Bitmap.createBitmap(
            sourceBitmap.width,
            sourceBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        Canvas(newBitmap).apply {
            drawColor(layout.color(picture.cardColor.bgColor))
            density = sourceBitmap.density
            drawBitmap(sourceBitmap, 0f, 0f, null)
            drawBitmap(bitmap, 0f, 0f, null)
        }
        saveBitmap(context, picture, newBitmap)
    }
}