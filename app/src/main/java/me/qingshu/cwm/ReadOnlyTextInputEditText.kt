package me.qingshu.cwm

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.View
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.TextInputEditText
import me.qingshu.cwm.extensions.getColorStateListByAttr

class ReadOnlyTextInputEditText @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr:Int = com.google.android.material.R.attr.editTextStyle
):TextInputEditText(context,attr,defStyleAttr) {

    init {
        setTextIsSelectable(isTextSelectable)
    }

    override fun getFreezesText(): Boolean = false

    override fun getDefaultEditable(): Boolean = false

    override fun getDefaultMovementMethod(): MovementMethod? = null

    override fun setTextIsSelectable(selectable: Boolean) {
        super.setTextIsSelectable(selectable)

        if(selectable){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                focusable = View.FOCUSABLE_AUTO
            }
        }else {
            isClickable = false
            isFocusable = false
        }
        background = background
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        var background = background
        if (isTextSelectable) {
            if (background is RippleDrawable) {
                val content = background.findDrawableByLayerId(0)
                if (content is MaterialShapeDrawable) {
                    background = content
                }
            }
        } else {
            if (background is MaterialShapeDrawable) {
                background = addRippleEffect(background)
            }
        }
        @Suppress("DEPRECATION")
        super.setBackgroundDrawable(background)
    }

    private fun addRippleEffect(boxBackground: MaterialShapeDrawable): Drawable {
        val rippleColor = context.getColorStateListByAttr(androidx.appcompat.R.attr.colorControlHighlight)
        val mask = MaterialShapeDrawable(boxBackground.shapeAppearanceModel)
            .apply { setTint(Color.WHITE) }
        return RippleDrawable(rippleColor, boxBackground, mask)
    }
}
