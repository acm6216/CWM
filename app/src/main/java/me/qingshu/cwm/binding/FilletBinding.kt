package me.qingshu.cwm.binding

import android.view.View
import androidx.core.view.forEach
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import me.qingshu.cwm.R
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferencePictureBinding
import me.qingshu.cwm.style.Styles

class FilletBinding(
    paramBinding: ParamBinding
):Binding<PreferencePictureBinding>(paramBinding) {

    companion object{
        const val FILLET_NONE = 0
        const val FILLET_CORNEL = 1
        const val FILLET_SHADOW = 1 shl 1
        const val FILLET_ALL = FILLET_CORNEL or FILLET_SHADOW
    }

    override val binding get() = get { it.picture }

    fun visible(styles: Styles){
        binding.root.visibility = if(styles.fillet) View.VISIBLE else View.GONE
    }

    fun bind(select:((Int)->Unit)?=null) = binding.apply {
        selectRoot.forEach {
            it.setOnClickListener {
                select?.invoke(flags(selectRoot))
            }
        }
    }

    private fun flags(group:ChipGroup):Int{
        val cornel = group.findViewById<Chip>(R.id.corner)
        val shadow = group.findViewById<Chip>(R.id.shadow)
        return if(cornel.isChecked && shadow.isChecked){
            FILLET_ALL
        }else if(!cornel.isChecked && shadow.isChecked){
            FILLET_SHADOW
        }else if(cornel.isChecked && !shadow.isChecked){
            FILLET_CORNEL
        }else FILLET_NONE
    }

}