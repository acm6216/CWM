package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.databinding.ScreenGravityBinding
import me.qingshu.cwm.screen.adapter.GravityPickerAdapter

class GravityScreen: BaseFragment() {

    private val binding by lazy { ScreenGravityBinding.inflate(layoutInflater) }
    private val viewmodel: MainViewModel by activityViewModels()
    private val gravityPickerAdapter = GravityPickerAdapter{
        viewmodel.receiveGravity(it)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = gravityPickerAdapter
        binding.recyclerView.itemAnimator = null

        repeatWithViewLifecycle {
            launch {
                viewmodel.gravityItems.collect{
                    gravityPickerAdapter.submitList(it)
                }
            }
        }
    }

}