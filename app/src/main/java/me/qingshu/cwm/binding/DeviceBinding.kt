package me.qingshu.cwm.binding

import android.os.Build
import android.view.View
import me.qingshu.cwm.data.Device
import me.qingshu.cwm.databinding.ParamBinding

class DeviceBinding(private val paramBinding: ParamBinding) {

    private val binding get() = paramBinding.device

    fun bind() {
        binding.info.setOnClickListener(::deviceInfo)
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