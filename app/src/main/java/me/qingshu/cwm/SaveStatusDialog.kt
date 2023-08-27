package me.qingshu.cwm

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.SaveStatus
import me.qingshu.cwm.databinding.SaveStatusBinding

class SaveStatusDialog:DialogFragment() {

    private val binding by lazy { SaveStatusBinding.inflate(layoutInflater) }
    private val viewModel:PictureViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = MaterialAlertDialogBuilder(requireContext()).apply {
        setTitle(R.string.save_status)
        setView(binding.root)
    }.create().also {
        it.setCanceledOnTouchOutside(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repeatWithViewLifecycle {
            launch {
                viewModel.saveStatus.collect {
                    it.setStatus()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun SaveStatus.setStatus(){
        binding.progress.apply {
            binding.status.text = "${currentProgress}/$pictureCount"
            max = pictureCount
            progress = currentProgress
        }
    }

    private inline fun Fragment.repeatWithViewLifecycle(
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        if (minState == Lifecycle.State.INITIALIZED || minState == Lifecycle.State.DESTROYED) {
            throw IllegalArgumentException("minState must be between INITIALIZED and DESTROYED")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(minState) {
                block()
            }
        }
    }

}