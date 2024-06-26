package me.qingshu.cwm.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat

class SpaceTextView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : LinearLayoutCompat(context, attr, defStyleAttr) {

    private val labelTextView:SpaceLabelTextView
    private val valueTextView:TextView

    init {
        val lp = LayoutParams(0,LayoutParams.WRAP_CONTENT,1f)
        addView(
            Space(context).also {
                it.layoutParams = lp
            }
        )
        addView(
            SpaceLabelTextView(context).also{
                labelTextView = it
                it.gravity = Gravity.CENTER
                it.layoutParams = LayoutParams(0,LayoutParams.WRAP_CONTENT,2f)
            }
        )
        addView(
            Space(context).also {
                it.layoutParams = lp
            }
        )
        addView(
            TextView(context).also{
                valueTextView = it
                it.gravity = Gravity.CENTER_VERTICAL or Gravity.END
                it.layoutParams = LayoutParams(0,LayoutParams.WRAP_CONTENT,3f)
            }
        )
        addView(
            Space(context).also {
                it.layoutParams = lp
            }
        )
        initParam()
    }

    var typeface:Typeface = Typeface.DEFAULT
        set(value) {
            field = value
            labelTextView.typeface = value
            valueTextView.typeface = value
        }
    var text:String = ""
        set(value) {
            field = value
            valueTextView.text = value
        }
    var label:String = ""
        set(value) {
            field = value
            labelTextView.text = value
        }

    var textSize:Float = 0f
        set(value) {
            field = value
            labelTextView.textSize = value
            valueTextView.textSize = value
        }

    var textColor:Int = 0xff000000.toInt()
        set(value) {
            field = value
            labelTextView.setTextColor(value)
            valueTextView.setTextColor(value)
        }

    fun setShadowLayer(radius: Float, dx: Float, dy: Float, color: Int) {
        labelTextView.setShadowLayer(radius, dx, dy, color)
        valueTextView.setShadowLayer(radius, dx, dy, color)
    }

    private fun initParam(){
        label = tag.toString()

        labelTextView.typeface = typeface

        valueTextView.typeface = typeface

        labelTextView.text = label
        valueTextView.text = text

        labelTextView.textSize = textSize
        valueTextView.textSize = textSize

        labelTextView.setTextColor(textColor)
        valueTextView.setTextColor(textColor)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        labelTextView.setPadding(left, top, right, bottom)
        valueTextView.setPadding(left, top, right, bottom)
    }
}