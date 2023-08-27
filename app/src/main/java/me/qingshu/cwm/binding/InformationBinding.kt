package me.qingshu.cwm.binding

import android.annotation.SuppressLint
import android.view.View
import me.qingshu.cwm.data.Information
import me.qingshu.cwm.databinding.ParamBinding
import java.text.SimpleDateFormat
import java.util.Date

class InformationBinding(private val paramBinding: ParamBinding) {

    private val binding get() = paramBinding.other

    fun bind(){
        binding.info.setOnClickListener(::bindDate)
        binding.locationInfo.setOnClickListener(::locationInfo)
    }

    @SuppressLint("SimpleDateFormat")
    private fun bindDate(view: View){
        SimpleDateFormat("yyyy.MM.dd")
            .format(Date(System.currentTimeMillis())).also {
                binding.date.setText(it)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun locationInfo(view: View){
        val result = "${binding.location.text} · "
        binding.location.setText(result)
        binding.location.setSelection(result.length)
    }

    fun getInformation() = Information(
        date = binding.date.text.toString(),
        location = binding.location.text.toString()
    )

    fun setInformation(info: Information){
        binding.date.setText(info.date)
        binding.location.setText(info.location)
    }
}