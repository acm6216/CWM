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

    var isPreview:Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        paint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val label = tag as String
        val broderWidth = paint.textSize / 2
        val offset = measuredWidth / 5
        val strokeW = paint.strokeWidth
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = broderWidth / 9
        canvas.drawRoundRect(
            offset + strokeW / 2,
            strokeW / 2,
            offset + textSize * 3 + broderWidth * 2 - strokeW / 2,
            if(isPreview) measuredHeight - strokeW*2 else textSize + broderWidth * 2f - strokeW / 2,
            textSize / 3,
            textSize / 3,
            paint
        )

        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeW
        canvas.drawText(
            text,
            0,
            text.length,
            offset/4 + textSize * 3 + broderWidth * 10 + broderWidth,
            (textSize * 2 + broderWidth) / 2,
            paint
        )
        val ts = paint.textSize
        paint.textSize = paint.textSize/4f*3f
        canvas.drawText(
            label,
            0,
            label.length,
            offset + (textSize * 3) / (2 + label.length / 2)+ts/2,
            (textSize * 2 + broderWidth) / 2+if(isPreview) ts/32 else ts/4,
            paint
        )
        paint.textSize = ts
    }

}