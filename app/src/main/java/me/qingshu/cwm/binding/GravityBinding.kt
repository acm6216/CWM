package me.qingshu.cwm.binding

import android.content.DialogInterface
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.data.StyleGravity
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceGravityBinding
import me.qingshu.cwm.extensions.edit
import me.qingshu.cwm.extensions.sharedPreferences
import me.qingshu.cwm.style.Styles

private const val STYLE_GRAVITY_KEY = "STYLE_GRAVITY_KEY"
class GravityBinding(
    paramBinding: ParamBinding
):Binding<PreferenceGravityBinding>(paramBinding) {

    override val binding get() = get { it.gravity }

    private lateinit var clickCallback: (StyleGravity) -> Unit

    fun visible(styles: Styles){
        binding.root.visibility = if(styles.gravityVisible) View.VISIBLE else View.GONE
    }

    fun bind(click:(StyleGravity)->Unit) = binding.apply {
        clickCallback = click
        gravityRoot.setOnClickListener(::stylePicker)
        initGravity()
    }

    private inline val getValue get() = sharedPreferences().getInt(STYLE_GRAVITY_KEY,0)

    private fun stylePicker(view: View){
        MaterialAlertDialogBuilder(view.context).apply {

            val array = IntArray(StyleGravity.values().size)
            setSingleChoiceItems(array.mapIndexed { index, _ -> StyleGravity.from(index).flagName(context) }.toTypedArray(),getValue,::gravityClick)
            show()
        }
    }

    private fun initGravity() {
        val target = StyleGravity.from(getValue)
        clickCallback.invoke(target)
        target.flagName(context).also {
            binding.gravityText.setText(it)
        }
    }

    private fun gravityClick(dialog: DialogInterface, which:Int){
        sharedPreferences().edit {
            putInt(STYLE_GRAVITY_KEY,which)
        }
        val target = StyleGravity.from(which)
        target.flagName(context).also {
            binding.gravityText.setText(it)
        }
        clickCallback.invoke(target)
        dialog.dismiss()
    }
}