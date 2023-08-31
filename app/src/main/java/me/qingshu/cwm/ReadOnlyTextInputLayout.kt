package me.qingshu.cwm

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.R
import com.google.android.material.textfield.TextInputLayout
import me.qingshu.cwm.extensions.getDrawableCompat

class ReadOnlyTextInputLayout @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr:Int = R.attr.textInputStyle
) :TextInputLayout(context,attr,defStyleAttr) {

    init {
        isExpandedHintEnabled = false
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)

        if (child is EditText) {
            setDropDown(!child.isTextSelectable)
        }
    }

    fun setDropDown(dropDown: Boolean) {
        if (dropDown) {
            endIconMode = END_ICON_CUSTOM
            endIconDrawable = context.getDrawableCompat(R.drawable.mtrl_ic_arrow_drop_down)
        } else {
            endIconMode = END_ICON_NONE
        }
    }

}