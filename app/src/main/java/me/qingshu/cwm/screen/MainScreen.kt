package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.data.Template
import me.qingshu.cwm.databinding.MainScreenBinding

class MainScreen: BaseFragment() {

    private val binding by lazy { MainScreenBinding.inflate(layoutInflater) }

    private val viewmodel: MainViewModel by activityViewModels()

    private val picturePicker =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri ->
            viewmodel.receivePicture(uri, requireContext().applicationContext)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager.apply {
            adapter = ScreenAdapter(
                createScreens(),childFragmentManager,viewLifecycleOwner.lifecycle
            )
            setPageTransformer(ZoomOutPageTransformer())
            isUserInputEnabled = false
            TabLayoutMediator(binding.tableLayout,this,true,true) { tab, position ->
                tab.setText(ScreenTab.values()[position].label)
                tab.setIcon(ScreenTab.values()[position].icon)
            }.attach()
        }

        repeatWithViewLifecycle {
            launch {
                viewmodel.picker.collect{
                    picturePicker.launch(arrayOf("image/*"))
                }
            }
        }

        initTemplate()
    }

    private fun createScreens() = ArrayList<BaseFragment>().apply {
        add(MenuScreen())
        add(StyleScreen())
        add(IconScreen())
        add(EffectScreen())
        add(ColorScreen())
        add(SizeScreen())
        add(GravityScreen())
        add(TemplateScreen())
    }

    private inline val getValue get() = sharedPreferences().getLong(TEMPLATE_KEY,0)
    private inline val getTemplateValue get() = sharedPreferences().getString(TEMPLATE_VALUE,"")?:""

    private fun initTemplate() {
        val value = getTemplateValue
        if (value.isEmpty())
            viewmodel.receiveTemplate(arrayListOf())
        else GsonBuilder().create().fromJson<ArrayList<Template>>(value,
            object : TypeToken<ArrayList<Template>>() {}.type).also {
            viewmodel.receiveTemplate(it,getValue)
        }
    }
}