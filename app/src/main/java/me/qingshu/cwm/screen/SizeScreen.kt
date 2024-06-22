package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.databinding.ScreenSizeBinding
import me.qingshu.cwm.screen.adapter.SizePickerAdapter

class SizeScreen: BaseFragment() {

    private val binding by lazy { ScreenSizeBinding.inflate(layoutInflater) }
    private val viewmodel: MainViewModel by activityViewModels()
    private val sizePickerAdapter = SizePickerAdapter{
        viewmodel.receiveSize(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = sizePickerAdapter
        binding.recyclerView.itemAnimator = null

        repeatWithViewLifecycle {
            launch {
                viewmodel.sizes.collect{
                    sizePickerAdapter.submitList(it)
                }
            }
        }

    }
}