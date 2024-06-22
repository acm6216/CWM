package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.LogoPicker
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.data.Logo
import me.qingshu.cwm.data.CardIcon
import me.qingshu.cwm.databinding.ScreenIconBinding
import me.qingshu.cwm.screen.adapter.IconPickerAdapter

class IconScreen: BaseFragment() {

    private val binding by lazy { ScreenIconBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()

    private val cardLOGOPickerAdapter = IconPickerAdapter{
        when(it.cardIcon){
            CardIcon.GONE -> viewModel.receiveLogoVisible(!it.isCheck)
            CardIcon.WORD_ART -> ArtSignatureDialog().show(childFragmentManager,javaClass.simpleName)
            CardIcon.LOGO -> LogoPicker(
                icons = Logo.CANNON,
                logoPicker = { icon ->
                    viewModel.receiveLogo(icon)
                }
            ).show(childFragmentManager,javaClass.simpleName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = cardLOGOPickerAdapter
        binding.recyclerView.itemAnimator = null
        repeatWithViewLifecycle {
            launch {
                viewModel.cardIconItem.collect{
                    cardLOGOPickerAdapter.submitList(it)
                }
            }
        }
    }

}