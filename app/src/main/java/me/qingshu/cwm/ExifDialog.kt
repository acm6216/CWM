package me.qingshu.cwm

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.data.ExifInformation
import me.qingshu.cwm.databinding.DialogExifBinding
import me.qingshu.cwm.extensions.aperture
import me.qingshu.cwm.extensions.date
import me.qingshu.cwm.extensions.focalLength
import me.qingshu.cwm.extensions.iso
import me.qingshu.cwm.extensions.lensModel
import me.qingshu.cwm.extensions.shutter

class ExifDialog(
    private val exif: ExifInterface? = null
) : DialogFragment() {

    private var _binding: DialogExifBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).apply {
            DialogExifBinding.inflate(layoutInflater).also {
                _binding = it
            }
            setView(binding.root)
            setTitle(R.string.exif_dialog)
            setPositiveButton(R.string.dialog_close,null)
        }.create().also {
            onViewCreated(binding.root,savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.recyclerView.scrollIndicators = View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
        }
        binding.recyclerView.adapter = ExifDetailAdapter().apply {
            val result = ArrayList<ExifInformation>()
            exif?.also {
                result.add(ExifInformation(label = R.string.exif_dialog_shutter,it.shutter()))
                result.add(ExifInformation(label = R.string.exif_dialog_iso,it.iso()))
                result.add(ExifInformation(label = R.string.exif_dialog_focal_length,it.focalLength()))
                result.add(
                    ExifInformation(
                        label = R.string.exif_dialog_focal_length_canon,
                        it.focalLength().takeIf { s ->
                            s.isNotEmpty()
                        }?.let { f -> "${f.toFloat()*1.6f}" }?:""
                    )
                )
                result.add(
                    ExifInformation(
                        label = R.string.exif_dialog_focal_length_other,
                        it.focalLength().takeIf { s ->
                            s.isNotEmpty()
                        }?.let { f -> "${f.toFloat()*1.5f}" }?:""
                    )
                )
                result.add(ExifInformation(label = R.string.exif_dialog_aperture,it.aperture()))
                result.add(ExifInformation(label = R.string.exif_dialog_lens_model,it.lensModel()))
                result.add(ExifInformation(label = R.string.exif_dialog_date,it.date()))
            }
            submitList(result)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}