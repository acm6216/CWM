package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.edit
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.R
import me.qingshu.cwm.data.Template
import me.qingshu.cwm.databinding.RenameDialogBinding
import me.qingshu.cwm.databinding.ScreenTemplateBinding
import me.qingshu.cwm.screen.adapter.TemplatePickerAdapter

const val TEMPLATE_KEY = "TEMPLATE_KEY"
const val TEMPLATE_VALUE = "TEMPLATE_VALUE"
class TemplateScreen: BaseFragment() {

    private val binding by lazy { ScreenTemplateBinding.inflate(layoutInflater) }

    private val viewModel:MainViewModel by activityViewModels()

    private val templatePickerAdapter = TemplatePickerAdapter(
        click = { template ->
            sharedPreferences().edit {
                putLong(TEMPLATE_KEY, template.key)
            }
            viewModel.loadTemplate(template, fromUser = true)
        },
        longClick = { view, template ->
            PopupMenu(view.context,view).apply{
                menuInflater.inflate(R.menu.template_menu,menu)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_delete -> viewModel.deleteTemplate(template){ ts ->
                            ts.store(0)
                        }
                        else -> renameTemplate(template.name,true,template)
                    }
                    true
                }
                show()
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.save.setOnClick {
            renameTemplate()
        }

        binding.recyclerView.adapter = templatePickerAdapter
        binding.recyclerView.itemAnimator = null
        repeatWithViewLifecycle {
            launch {
                viewModel.templateItems.collect{
                    templatePickerAdapter.submitList(it)
                }
            }
        }
    }

    private fun List<Template>.store(key:Long = 0){
        GsonBuilder().create().toJson(this).also {
            sharedPreferences().edit {
                putString(TEMPLATE_VALUE,it)
            }
        }
        if(key>0) sharedPreferences().edit {
            putLong(TEMPLATE_KEY,key)
        }
    }

    private fun renameTemplate(name:String = "",modifier:Boolean = false,targetTemplate: Template? = null){
        val layout = RenameDialogBinding.inflate(LayoutInflater.from(binding.root.context))
        layout.input.setText(
            name.ifEmpty { "${viewModel.deviceBrand.value} ${viewModel.deviceModel.value}" }
        )
        MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(layout.root)
            setTitle(R.string.template_rename)
            setPositiveButton(R.string.template_apply){ _, _ ->
                val targetName = layout.input.text.toString()
                if (targetName.trim().replace("\n","").isNotEmpty()) {
                    if(modifier) {
                        viewModel.modifierTemplate(targetName,targetTemplate!!){
                            it.store()
                        }
                    }else viewModel.saveTemplate(targetName){ list,key -> list.store(key) }
                }
            }
            setNegativeButton(R.string.template_cancel,null)
            show()
        }
    }
}