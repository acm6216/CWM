package me.qingshu.cwm.binding

import android.content.DialogInterface
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.style.Styles
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceStylesBinding
import me.qingshu.cwm.extensions.edit
import me.qingshu.cwm.extensions.sharedPreferences

private const val STYLE_KEY = "STYLE_KEY"

class StyleBinding(paramBinding: ParamBinding):Binding<PreferenceStylesBinding>(paramBinding) {

    override val binding get() = get { it.styles }

    private lateinit var clickCallback: (Styles,Boolean) -> Unit

    fun bind(click:(Styles,Boolean)->Unit) = binding.apply {
        clickCallback = click
        stylesRoot.setOnClickListener(::stylePicker)
        initStyle()
    }

    private fun sharedPreferences() = context.sharedPreferences()

    private inline val getValue get() = sharedPreferences().getInt(STYLE_KEY,0)

    private fun stylePicker(view: View){
        MaterialAlertDialogBuilder(view.context).apply {

            val array = IntArray(Styles.values().size)
            setSingleChoiceItems(array.mapIndexed { index, _ -> Styles.from(index).styleName(context) }.toTypedArray(),getValue,::styleClick)
            show()
        }
    }

    private fun initStyle() {
        val target = Styles.from(getValue)
        clickCallback.invoke(target,false)
        target.styleName(context).also {
            binding.stylesText.setText(it)
        }
    }

    private fun styleClick(dialog: DialogInterface, which:Int){
        sharedPreferences().edit {
            putInt(STYLE_KEY,which)
        }
        val target = Styles.from(which)
        target.styleName(context).also {
            binding.stylesText.setText(it)
        }
        clickCallback.invoke(target,true)
        dialog.dismiss()
    }

}