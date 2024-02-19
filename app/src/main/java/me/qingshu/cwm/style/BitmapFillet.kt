package me.qingshu.cwm.style

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

const val CORNER_NONE = 0
const val CORNER_TOP_LEFT = 1
const val CORNER_TOP_RIGHT = 1 shl 1
const val CORNER_BOTTOM_LEFT = 1 shl 2
const val CORNER_BOTTOM_RIGHT = 1 shl 3
const val CORNER_ALL = CORNER_TOP_LEFT or CORNER_TOP_RIGHT or CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
const val CORNER_TOP = CORNER_TOP_LEFT or CORNER_TOP_RIGHT
const val CORNER_BOTTOM = CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
const val CORNER_LEFT = CORNER_TOP_LEFT or CORNER_BOTTOM_LEFT
const val CORNER_RIGHT = CORNER_TOP_RIGHT or CORNER_BOTTOM_RIGHT

fun Bitmap.radius(roundPx: Int, corners: Int): Bitmap {
    val width = width
    val height = height

    val paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(paintingBoard)
    canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.color = Color.BLACK

    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    canvas.drawRoundRect(rectF, roundPx.toFloat(), roundPx.toFloat(), paint)

    val notRoundedCorners = corners xor CORNER_ALL
    if ((notRoundedCorners and CORNER_TOP_LEFT) != 0) {
        clipTopLeft(canvas, paint, roundPx, width, height)
    }
    if ((notRoundedCorners and CORNER_TOP_RIGHT) != 0) {
        clipTopRight(canvas, paint, roundPx, width, height)
    }
    if ((notRoundedCorners and CORNER_BOTTOM_LEFT) != 0) {
        clipBottomLeft(canvas, paint, roundPx, width, height)
    }
    if ((notRoundedCorners and CORNER_BOTTOM_RIGHT) != 0) {
        clipBottomRight(canvas, paint, roundPx, width, height)
    }
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    val src = Rect(0, 0, width, height)
    canvas.drawBitmap(this, src, src, paint)
    return paintingBoard
}

private fun clipTopLeft(canvas: Canvas, paint: Paint, offset: Int, width: Int, height: Int) {
    val block = Rect(0, 0, offset, offset)
    canvas.drawRect(block, paint)
}

private fun clipTopRight(canvas: Canvas, paint: Paint, offset: Int, width: Int, height: Int) {
    val block = Rect(width - offset, 0, width, offset)
    canvas.drawRect(block, paint)
}

private fun clipBottomLeft(canvas: Canvas, paint: Paint, offset: Int, width: Int, height: Int) {
    val block = Rect(0, height - offset, offset, height)
    canvas.drawRect(block, paint)
}

private fun clipBottomRight(canvas: Canvas, paint: Paint, offset: Int, width: Int, height: Int) {
    val block = Rect(width - offset, height - offset, width, height)
    canvas.drawRect(block, paint)
}

fun Bitmap.shadow(size:Float,textColorValue:Int,isRadius:Boolean = false,isShadow:Boolean = false):Bitmap{
    val shadowPaint = Paint()
    val color = Color.argb(
        200,
        textColorValue.red,
        textColorValue.green,
        textColorValue.blue
    )
    val sr = if(isRadius) size else 0f
    val shadowRadius = if(isShadow) size/2f else 0f

    shadowPaint.color = color
    shadowPaint.style = Paint.Style.FILL
    shadowPaint.isAntiAlias = true
    val right = width + size
    val bottom = height + size

    shadowPaint.setShadowLayer(shadowRadius, 0f, 0f, color)
    shadowPaint.strokeJoin = Paint.Join.ROUND
    val rectF = RectF(size, size, right, bottom)
    val result = Bitmap.createBitmap(width+(size*2).toInt(),height+(size*2).toInt(),Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    canvas.drawRoundRect(rectF, sr, sr, shadowPaint)
    canvas.drawBitmap(this,size,size,Paint().apply { isAntiAlias=true })
    return result
}