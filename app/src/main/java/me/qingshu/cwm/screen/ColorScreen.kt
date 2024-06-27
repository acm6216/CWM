package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.data.CardColor
import me.qingshu.cwm.databinding.ScreenColorBinding
import me.qingshu.cwm.screen.adapter.ColorPickerAdapter

class ColorScreen: BaseFragment() {

    private val binding by lazy { ScreenColorBinding.inflate(layoutInflater) }
    private val viewmodel: MainViewModel by activityViewModels()
    private val colorPickerAdapter = ColorPickerAdapter(
        click = {
            viewmodel.receiveColor(it)
        },
        colorPicker = { defColor,isTextColor,colors,position ->
            ColorPickerDialog(
                def = defColor,
                colors = colors,
                unit = {
                    binding.recyclerView.store()
                    if(isTextColor) CardColor.storeCustomTextColor(requireContext(),it){
                        binding.recyclerView.adapter?.notifyItemChanged(position-1)
                    }
                    else CardColor.storeCustomBackgroundColor(requireContext(),it){
                        binding.recyclerView.adapter?.notifyItemChanged(position-2)
                    }
                    binding.recyclerView.adapter?.notifyItemChanged(position)
                    viewmodel.customColorChange()
                    binding.recyclerView.onRestore()
                }
            ).show(childFragmentManager,javaClass.simpleName)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = colorPickerAdapter
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.storeRecyclerViewState()
        repeatWithViewLifecycle {
            launch {
                viewmodel.colors.collect{
                    binding.recyclerView.store()
                    colorPickerAdapter.submitList(it)
                    delay(20)
                    binding.recyclerView.onRestore()
                }
            }
        }
    }
}