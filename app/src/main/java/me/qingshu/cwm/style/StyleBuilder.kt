package me.qingshu.cwm.style

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import me.qingshu.cwm.data.Picture

/**
 * 处理（生成图片、阴影、圆角、保存）图片
 */
abstract class StyleBuilder<T: ViewBinding>(
    protected val _binding:T
) {

    abstract fun execute(context: Context, picture: Picture,coroutineScope:CoroutineScope)

    protected fun saveBitmap(context:Context,it: Picture,newBitmap:Bitmap){
        val file = DocumentFile.fromSingleUri(context, it.uri)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            MediaStore.Images.Media.insertImage(
                context.contentResolver, newBitmap, file?.name ?: "", "Camera Water Mark"
            )
        else context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file?.name ?: "")
                put(MediaStore.Images.Media.DESCRIPTION, file?.name ?: "")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
        )?.also { uri ->
            context.contentResolver.openOutputStream(uri)?.use { os ->
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            }
        }
    }

    protected inline val Int.dp: Int get() = run { toFloat().dp.toInt() }
    protected inline val Float.dp: Float get() = run {
        val scale: Float = _binding.root.resources.displayMetrics.density
        (this * scale + 0.5f)
    }
}