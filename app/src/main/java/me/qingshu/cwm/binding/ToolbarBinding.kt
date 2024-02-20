package me.qingshu.cwm.binding

import android.view.View
import me.qingshu.cwm.R
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceToolbarBinding
import me.qingshu.cwm.extensions.edit
import me.qingshu.cwm.style.Styles

private const val LOGO_VISIBLE_KEY = "LOGO_VISIBLE_KEY"
class ToolbarBinding(paramBinding: ParamBinding) : Binding<PreferenceToolbarBinding>(paramBinding) {

    override val binding get() = get { it.bar }

    private var isCheck:Boolean = true
        set(value) {
            field = value
            setIcon()
        }

    private fun setIcon(){
        binding.logoVisible.setImageResource(if (isCheck) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off)
    }

    fun visible(styles: Styles){
        binding.logoVisible.visibility = if(styles.iconVisible) View.VISIBLE else View.GONE
    }

    private inline val getValue get() = sharedPreferences().getBoolean(LOGO_VISIBLE_KEY,true)

    fun bind(
        logoPicker: ((View) -> Unit)? = null,
        picturePicker: (() -> Unit)? = null,
        visible: ((Boolean) -> Unit)? = null
    ) =
        binding.apply {
            logo.setOnClickListener { logoPicker?.invoke(it) }
            picker.setOnClickListener { picturePicker?.invoke() }
            logoVisible.setOnClickListener {
                isCheck = !isCheck
                visible?.invoke(isCheck)
                save()
            }
            isCheck = getValue
            visible?.invoke(isCheck)
        }

    private fun save(){
        sharedPreferences().edit {
            putBoolean(LOGO_VISIBLE_KEY,isCheck)
        }
    }
}