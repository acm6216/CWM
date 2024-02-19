package me.qingshu.cwm.binding

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.qingshu.cwm.R
import me.qingshu.cwm.data.BaseBoAdapter
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.data.CardSize
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.data.Icon
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.data.Template
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceTemplateBinding
import me.qingshu.cwm.databinding.RenameDialogBinding
import me.qingshu.cwm.extensions.edit

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
        templateRoot.setOnLongClickListener(::renameTemplate)
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
        name:String = "",
        fromButton:Boolean = false
    ) {
        val index = getValue
        Template(
            device = device,
            information = information,
            lens = lens,
            cardSize = cardSize,
            cardColor = cardColor,
            name = if(fromButton) Template.USE[getValue].name else name
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

    private fun renameTemplate(view: View):Boolean{
        val layout = RenameDialogBinding.inflate(LayoutInflater.from(view.context))
        layout.input.setText(Template.USE[getValue].name)
        MaterialAlertDialogBuilder(view.context).apply {
            setView(layout.root)
            setTitle(R.string.template_rename)
            setPositiveButton(R.string.template_save){_,_ ->
                val targetName = layout.input.text.toString()
                if (targetName.trim().replace("\n","").isNotEmpty()) {
                    Template.USE[getValue].run {
                        save(
                            device = device,
                            information = information,
                            lens = lens,
                            cardSize = cardSize,
                            cardColor = cardColor,
                            name = targetName
                        )
                    }
                    binding.templateText.setText(targetName)
                }
            }
            setNegativeButton(R.string.template_cancel,null)
            show()
        }
        return true
    }

    private fun initTemplate() {
        Template.USE.clear()
        val value = getTemplateValue
        if (value.isEmpty())
            repeat(Template.MAX) { index ->
                Template(
                    device = Device.empty,
                    lens = Lens.empty,
                    information = Information.empty,
                    cardSize = CardSize.LARGE,
                    cardColor = CardColor.WHITE,
                    name = "模板 ${index+1}"
                ).also { Template.USE.add(it) }
            }
        else GsonBuilder().registerTypeAdapter(Icon::class.java, BaseBoAdapter()).create().fromJson<ArrayList<Template>>(value,
            object : TypeToken<ArrayList<Template>>() {}.type).also {
            Template.USE.addAll(it)
        }

        binding.templateText.setText(Template.USE[getValue].name)
        clickCallback.invoke(getTemplate(getValue))
    }

    private fun templatePicker(view: View){
        MaterialAlertDialogBuilder(view.context).apply {

            setSingleChoiceItems(Template.USE.map {it.name }.toTypedArray(),getValue,::templateClick)
            show()
        }
    }

    private fun templateClick(dialog:DialogInterface, which:Int){
        sharedPreferences().edit {
            putInt(TEMPLATE_KEY,which)
        }
        val target = getTemplate(which)
        binding.templateText.setText(target.name)
        clickCallback.invoke(target)
        dialog.dismiss()
    }

    private fun getTemplate(index:Int) = Template.USE[index]

}