package me.qingshu.cwm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.DialogFragment
import me.qingshu.cwm.databinding.DialogExifBinding
import me.qingshu.cwm.extensions.aperture
import me.qingshu.cwm.extensions.date
import me.qingshu.cwm.extensions.device
import me.qingshu.cwm.extensions.focalLength
import me.qingshu.cwm.extensions.iso
import me.qingshu.cwm.extensions.lensModel
import me.qingshu.cwm.extensions.pictureNumber
import me.qingshu.cwm.extensions.shutter

class ExifDialog(
    private val exif: ExifInterface? = null
) : DialogFragment() {

    private var _binding: DialogExifBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogExifBinding.inflate(layoutInflater).also {
        _binding = it
    }.root

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exif?.also {
            binding.result.text = view.resources.getString(
                R.string.exif_dialog,
                it.shutter(),
                it.date(),
                it.aperture(),
                it.device(),
                it.focalLength(),
                it.iso(),
                it.lensModel(),
                it.pictureNumber()
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}