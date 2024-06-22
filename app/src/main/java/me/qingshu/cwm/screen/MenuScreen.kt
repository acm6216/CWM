package me.qingshu.cwm.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.launch
import me.qingshu.cwm.BaseFragment
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.data.CardMenu
import me.qingshu.cwm.databinding.ScreenPictureBinding
import me.qingshu.cwm.screen.adapter.MenuPickerAdapter

class MenuScreen : BaseFragment() {

    private val binding by lazy { ScreenPictureBinding.inflate(layoutInflater) }
    private val viewmodel: MainViewModel by activityViewModels()
    private val mainPickerAdapter = MenuPickerAdapter{
        when(it.cardMenu){
            CardMenu.PICKER -> viewmodel.picker()
            CardMenu.EXIF -> ExifScreen().show(childFragmentManager,javaClass.simpleName)
            else -> viewmodel.save()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = mainPickerAdapter
        binding.recyclerView.itemAnimator = null

        repeatWithViewLifecycle {
            launch {
                viewmodel.mainMenu.collect{
                    mainPickerAdapter.submitList(it)
                }
            }
        }

    }

}