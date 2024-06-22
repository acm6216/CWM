package me.qingshu.cwm

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PreviewBinding
import me.qingshu.cwm.extensions.fadeToVisibilityUnsafe
import me.qingshu.cwm.style.adapter.PictureAdapter

class Preview : BaseFragment() {

    private val binding by lazy { PreviewBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by activityViewModels()

    private fun onMenuItemClick(view: View, picture: Picture){
        PopupMenu(view.context,view).apply {
            menuInflater.inflate(R.menu.option_menu,menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_remove -> mainViewModel.removePicture(picture)
                    R.id.menu_clear -> mainViewModel.removeAllPicture()
                    R.id.menu_exif -> mainViewModel.exif(picture,view.context)
                }
                true
            }
            show()
        }
    }

    private val pictureAdapter = PictureAdapter(::onMenuItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = mainViewModel
        binding.bindLifecycle()
        binding.recyclerView.adapter = pictureAdapter
        binding.recyclerView.itemAnimator = null
        repeatWithViewLifecycle {
            launch {
                mainViewModel.previewPictures.collect {
                    pictureAdapter.submitList(it)
                }
            }
            launch {
                mainViewModel.saveEnable.collect{
                    binding.indicator.fadeToVisibilityUnsafe(!it)
                }
            }
            launch {
                mainViewModel.message.collect{
                    showMessage(it)
                }
            }
            launch {
                mainViewModel.pictureExif.collect{ e ->
                    e.also {  ExifDialog(it).show(childFragmentManager,javaClass.simpleName) }
                }
            }
            launch {
                mainViewModel.save.collect{
                    save()
                }
            }
        }
        binding.copyright.also {
            val content = SpannableString(it.text.toString())
            content.setSpan(UnderlineSpan(), 17, 24, 0)
            it.text = content
        }

        val packageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
        binding.version.text = getString(R.string.label_version,packageInfo.versionName)
    }

    private fun showMessage(@StringRes strRes:Int){
        if(strRes!=0) {
            Snackbar.make(binding.recyclerView, strRes, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun save(){
        mainViewModel.save(requireContext().applicationContext,binding)
    }

}