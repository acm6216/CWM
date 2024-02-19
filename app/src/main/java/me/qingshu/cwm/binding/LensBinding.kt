package me.qingshu.cwm.binding

import me.qingshu.cwm.R
import me.qingshu.cwm.data.Lens
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceLensBinding

class LensBinding(paramBinding: ParamBinding):Binding<PreferenceLensBinding>(paramBinding) {

    override val binding get() = get { it.lens }

    private var isCheck:Boolean = true
        set(value) {
            field = value
            binding.paramRoot.setEndIconDrawable(updateEndIcon())
        }

    private fun updateEndIcon() = if (isCheck) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off

    fun bind() {
        binding.paramRoot.apply {
            setEndIconDrawable(updateEndIcon())
            isEndIconVisible = true
            setEndIconOnClickListener {
                isCheck = !isCheck
                setEndIconDrawable(updateEndIcon())
            }
        }
    }

    fun getLens() = Lens(
        param = binding.param.text.toString(),
        focalDistance = binding.focalDistance.text.toString(),
        aperture = binding.aperture.text.toString(),
        shutter = binding.shutter.text.toString(),
        iso = binding.iso.text.toString(),
        paramVisible = isCheck
    )

    fun setLens(lens: Lens){
        binding.param.setText(lens.param)
        binding.focalDistance.setText(lens.focalDistance)
        binding.aperture.setText(lens.aperture)
        binding.shutter.setText(lens.shutter)
        binding.iso.setText(lens.iso)
        isCheck = lens.paramVisible
    }
}