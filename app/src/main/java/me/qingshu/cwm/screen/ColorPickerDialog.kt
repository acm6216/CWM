package me.qingshu.cwm.screen

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.databinding.ColorPickerBinding

class ColorPickerDialog(
    private val def:Int = 0,
    private val colors:Int = 0,
    private val unit:((Int)->Unit)?=null
):DialogFragment() {

    private lateinit var _binding: ColorPickerBinding
    private val binding: ColorPickerBinding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setView(ColorPickerBinding.inflate(layoutInflater).also {
                _binding = it
            }.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lobsterPicker.apply {
            setColorHistoryEnabled(true)
            addDecorator(binding.shadeSlider)
            setHistory(colors)
            setColor(colors)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.scrollIndicators = View.SCROLL_INDICATOR_BOTTOM or View.SCROLL_INDICATOR_TOP
        }
        binding.close.setOnClickListener { dismiss() }
        binding.ok.setOnClickListener {
            unit?.invoke(binding.lobsterPicker.getColor())
            dismiss()
        }
        binding.def.setOnClickListener {
            unit?.invoke(def)
            dismiss()
        }
    }

}