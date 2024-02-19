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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PreviewBinding
import me.qingshu.cwm.extensions.fadeToVisibilityUnsafe
import me.qingshu.cwm.style.adapter.PictureAdapter

class Preview : BaseFragment() {

    private val binding by lazy { PreviewBinding.inflate(layoutInflater) }

    private val viewModel: PictureViewModel by activityViewModels()

    private fun onMenuItemClick(view: View, picture: Picture){
        PopupMenu(view.context,view).apply {
            menuInflater.inflate(R.menu.option_menu,menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_remove -> viewModel.removePicture(picture)
                    R.id.menu_clear -> viewModel.removeAllPicture()
                    R.id.menu_exif -> viewModel.exif(picture,view.context)
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
        binding.model = viewModel
        binding.bindLifecycle()
        binding.save.setOnClick(::save)
        binding.recyclerView.adapter = pictureAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        repeatWithViewLifecycle {
            launch {
                viewModel.previewPictures.collect {
                    pictureAdapter.submitList(it)
                }
            }
            launch {
                viewModel.saveEnable.collect{
                    binding.indicator.fadeToVisibilityUnsafe(!it)
                }
            }
            launch {
                viewModel.message.collect{
                    showMessage(it)
                }
            }
            launch {
                viewModel.pictureExif.collect{ e ->
                    e.also {  ExifDialog(it).show(childFragmentManager,javaClass.simpleName) }
                }
            }
        }
        binding.copyright.also {
            val content = SpannableString(it.text.toString())
            content.setSpan(UnderlineSpan(), 17, 24, 0)
            it.text = content
        }

    }

    private fun showMessage(@StringRes strRes:Int){
        if(strRes!=0) {
            Snackbar.make(binding.save, strRes, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun save(){
        viewModel.save(requireContext().applicationContext,binding)
    }

}