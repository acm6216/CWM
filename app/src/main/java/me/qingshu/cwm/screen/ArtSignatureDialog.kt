package me.qingshu.cwm.screen

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
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.R
import me.qingshu.cwm.data.ArtSignature
import me.qingshu.cwm.databinding.ArtSignatureBinding
import me.qingshu.cwm.screen.adapter.ArtSignatureAdapter

class ArtSignatureDialog:DialogFragment() {

    private val binding: ArtSignatureBinding by lazy { ArtSignatureBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()
    private val artSignatureAdapter = ArtSignatureAdapter{
        viewModel.receiveFont(it)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(binding.root)
            setTitle(R.string.art_signature_title)
            setPositiveButton(R.string.template_apply) { _, _ ->
                apply()
            }
            setNegativeButton(R.string.template_cancel, null)
        }.create().also {
            onCreateView(layoutInflater,null,savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    private fun apply(){
        val targetName = binding.input.text.toString()
        if (targetName.trim().replace("\n", "").isNotEmpty()) {
            viewModel.receiveArtSignature(targetName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this@ArtSignatureDialog
        binding.executePendingBindings()

        binding.recyclerView.adapter = artSignatureAdapter
        binding.recyclerView.itemAnimator = null

        binding.inputRoot.setEndIconOnClickListener {
            viewModel.toggleArtSignatureVisible()
        }

        repeatWithViewLifecycle {
            launch {
                viewModel.artSignature.collect{
                    binding.inputRoot.typeface = it.typeface(requireContext())
                }
            }
            launch {
                viewModel.artSignatureItem.collect{
                    artSignatureAdapter.submitList(it)
                }
            }
            launch {
                viewModel.artSignatureVisible.collect{
                    binding.inputRoot.setEndIconDrawable(updateEndIcon(it))
                    binding.input.isEnabled = it
                }
            }
        }
    }

    private fun updateEndIcon(isCheck:Boolean) = if (isCheck) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off

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