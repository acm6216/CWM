package me.qingshu.cwm.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class SpaceLabelTextView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attr, defStyleAttr) {

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        paint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val broderWidth = paint.textSize / 10
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = broderWidth
        canvas.drawRect(
            broderWidth,
            broderWidth,
            measuredWidth - broderWidth,
            measuredHeight - broderWidth,
            paint
        )
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)
    }

}