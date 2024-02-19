package me.qingshu.cwm.binding

import android.content.DialogInterface
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.qingshu.cwm.R
import me.qingshu.cwm.data.BaseBoAdapter
import me.qingshu.cwm.data.CameraLogo
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Icon
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Template
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceTemplateBinding
import me.qingshu.cwm.extensions.edit
import me.qingshu.cwm.extensions.sharedPreferences

private const val TEMPLATE_KEY = "TEMPLATE_KEY"
private const val TEMPLATE_VALUE = "TEMPLATE_VALUE"

class TemplateBinding(paramBinding: ParamBinding):Binding<PreferenceTemplateBinding>(paramBinding) {

    override val binding get() = get { it.template }

    private inline val getValue get() = sharedPreferences().getInt(TEMPLATE_KEY,0)
    private inline val getTemplateValue get() = sharedPreferences().getString(TEMPLATE_VALUE,"")?:""
    private lateinit var clickCallback: (Template) -> Unit

    fun bind(
        click:(Template)->Unit,
        saveCallback:((TemplateBinding)->Unit)?=null
    ) = binding.apply {
        templateRoot.setOnClickListener(::templatePicker)
        getValue.let {
            context.getString(R.string.template_name,it+1)
        }.also {
            templateText.setText(it)
        }
        saveTemplate.setOnClickListener{
            saveCallback?.invoke(this@TemplateBinding)
        }
        clickCallback = click
        initTemplate()
    }

    fun save(
        device: Device,
        information: Information,
        lens: Lens,
        cardSize: CardSize,
        cardColor: CardColor,
        icon: Icon
    ) {
        val index = getValue
        Template(
            device = device,
            information = information,
            lens = lens,
            cardSize = cardSize,
            cardColor = cardColor,
            //icon = icon
        ).also {
            Template.USE.run {
                add(index, it)
                removeAt(index + 1)
                store()
            }
        }
    }

    private fun ArrayList<Template>.store(){
        GsonBuilder().registerTypeAdapter(Icon::class.java, BaseBoAdapter()).create().toJson(this).also {
            sharedPreferences().edit {
                putString(TEMPLATE_VALUE,it)
            }
        }
    }

    private fun initTemplate() {
        Template.USE.clear()
        val value = getTemplateValue
        if (value.isEmpty())
            repeat(Template.MAX) {
                Template(
                    //icon = CameraLogo.CANNON,
                    device = Device.empty,
                    lens = Lens.empty,
                    information = Information.empty,
                    cardSize = CardSize.LARGE,
                    cardColor = CardColor.WHITE
                ).also { Template.USE.add(it) }
            }
        else GsonBuilder().registerTypeAdapter(Icon::class.java, BaseBoAdapter()).create().fromJson<ArrayList<Template>>(value,
            object : TypeToken<ArrayList<Template>>() {}.type).also {
            Template.USE.addAll(it)
        }
        clickCallback.invoke(getTemplate(getValue))
    }

    private fun templatePicker(view: View){
        MaterialAlertDialogBuilder(view.context).apply {
            val name = context.getString(R.string.template_title)
            val array = IntArray(Template.MAX)
            setSingleChoiceItems(array.mapIndexed { index, _ ->  "$name ${index+1}" }.toTypedArray(),getValue,::templateClick)
            show()
        }
    }

    private fun templateClick(dialog:DialogInterface, which:Int){
        sharedPreferences().edit {
            putInt(TEMPLATE_KEY,which)
        }
        context.getString(R.string.template_name,which+1).also {
            binding.templateText.setText(it)
        }
        clickCallback.invoke(getTemplate(which))
        dialog.dismiss()
    }

    private fun getTemplate(index:Int) = Template.USE[index]

    private fun sharedPreferences() = context.sharedPreferences()

}