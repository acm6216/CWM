package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.databinding.ScreenStyleBinding
import me.qingshu.cwm.extensions.edit
import me.qingshu.cwm.screen.adapter.StylePickerAdapter

private const val STYLE_KEY = "STYLE_KEY"
class StyleScreen:BaseFragment() {

    private val binding by lazy { ScreenStyleBinding.inflate(layoutInflater) }
    private val viewmodel: MainViewModel by activityViewModels()
    private val stylePickerAdapter = StylePickerAdapter{
        viewmodel.receiveStyle(it)
        setStyle(it.ordinal)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = stylePickerAdapter
        binding.recyclerView.itemAnimator = null
        repeatWithViewLifecycle {
            launch {
                viewmodel.styles.collect{
                    stylePickerAdapter.submitList(it)
                }
            }
        }
    }

    private inline val getValue get() = sharedPreferences().getInt(STYLE_KEY,0)

    private fun setStyle(which:Int){
        sharedPreferences().edit {
            putInt(STYLE_KEY,which)
        }
    }
}