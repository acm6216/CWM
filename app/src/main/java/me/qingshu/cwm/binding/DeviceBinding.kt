package me.qingshu.cwm.binding

import android.os.Build
import android.view.View
import me.qingshu.cwm.R
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.databinding.ParamBinding
import me.qingshu.cwm.databinding.PreferenceDeviceBinding

class DeviceBinding(
    paramBinding: ParamBinding
):Binding<PreferenceDeviceBinding>(paramBinding) {

    override val binding get() = get { it.device }

    fun bind() {
        binding.info.setOnClickListener(::deviceInfo)
        binding.symbol.setOnClickListener(::symbol)
    }

    private fun symbol(view: View){
        val symbols = view.context.resources.getStringArray(R.array.model_symbol)
        binding.modelInput.append(symbols[0])
    }

    private fun deviceInfo(view: View){
        setDevice(Device(Build.BRAND,Build.MODEL))
    }

    fun getDevice() = Device(
        brand = binding.brandInput.text.toString(),
        model = binding.modelInput.text.toString()
    )

    fun setDevice(device: Device){
        binding.brandInput.setText(device.brand)
        binding.modelInput.setText(device.model)
    }

}