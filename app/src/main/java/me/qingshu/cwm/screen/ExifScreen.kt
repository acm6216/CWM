package me.qingshu.cwm.screen

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.R
import me.qingshu.cwm.MainViewModel
import me.qingshu.cwm.StringPicker
import me.qingshu.cwm.databinding.ScreenExifBinding
import me.qingshu.cwm.style.Styles
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExifScreen: DialogFragment() {

    private val binding by lazy { ScreenExifBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root).apply {
                setTitle(R.string.screen_exif_title)
                setPositiveButton(R.string.screen_exif_apply) { _, _ ->
                    mainViewModel.sendExif()
                }
                setNegativeButton(R.string.screen_exif_close,null)
            }.create().also {
                onCreated()
            }

    private var isCheck:Boolean = true
        set(value) {
            field = value
            binding.paramRoot.setEndIconDrawable(updateEndIcon())
            binding.param.isEnabled = value
            binding.focalDistance.isEnabled = !value
            binding.aperture.isEnabled = !value
            binding.shutter.isEnabled = !value
            binding.iso.isEnabled = !value
            mainViewModel.lensParamVisible.value = value
        }


    fun visible(styles: Styles){
        binding.informationRoot.visibility = if(styles.visibleLens) View.VISIBLE else View.GONE
    }

    private fun updateEndIcon() = if (isCheck) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off

    private fun onCreated()  = binding.apply{
        binding.viewmodel = mainViewModel
        binding.lifecycleOwner = this@ExifScreen
        binding.executePendingBindings()
        paramRoot.apply {
            setEndIconDrawable(updateEndIcon())
            isEndIconVisible = true
            setEndIconOnClickListener {
                isCheck = !isCheck
                setEndIconDrawable(updateEndIcon())
            }
        }
        dateRoot.setEndIconOnClickListener { getDeviceDate() }

        locationRoot.setEndIconOnClickListener {
            stringPicker(
                array = resources.getStringArray(R.array.string_symbols),
                auto = "",
                title = R.string.symbol_picker_title
            ){
                binding.location.append(it)
            }
        }

        binding.model.setEndIconOnClickListener {
            stringPicker(
                array = resources.getStringArray(R.array.string_symbols),
                auto = Build.MODEL,
                title = R.string.symbol_picker_title
            ){
                binding.modelInput.append(it)
            }

        }
        binding.brand.setEndIconOnClickListener {
            stringPicker(
                array = resources.getStringArray(R.array.string_devices),
                auto = Build.BRAND,
                title = R.string.device_title
            ){
                binding.brandInput.setText(it)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScroll.scrollIndicators = View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
        }

        isCheck = mainViewModel.lensParamVisible.value
    }

    private fun getDeviceDate() = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        .format(Date(System.currentTimeMillis())).also {
            binding.date.setText(it)
        }

    private fun stringPicker(
        array:Array<String>,
        auto:String,
        title:Int,
        callback:(String)->Unit
    ){
        StringPicker(array, auto, title, callback).show(childFragmentManager,javaClass.simpleName)
    }

}