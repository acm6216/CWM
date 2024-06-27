package me.qingshu.cwm

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
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
                    R.id.menu_save -> mainViewModel.savePicture(requireContext().applicationContext,binding,picture)
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = mainViewModel
        binding.bindLifecycle()
        binding.recyclerView.adapter = pictureAdapter
        AnimationUtils.loadAnimation(requireContext().applicationContext, R.anim.recycler_view_anim).also {
            val la = LayoutAnimationController(it)
            la.delay = 0.2f
            la.order = LayoutAnimationController.ORDER_NORMAL
            binding.recyclerView.layoutAnimation = la
        }
        binding.recyclerView.storeRecyclerViewState()

        repeatWithViewLifecycle {
            launch {
                mainViewModel.previewPictures.collect {
                    binding.recyclerView.store()
                    pictureAdapter.submitList(it)
                    delay(20)
                    binding.recyclerView.onRestore()

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
            launch {
                mainViewModel.customColor.collect{
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
        binding.copyright.also {
            val content = SpannableString(it.text.toString())
            content.setSpan(UnderlineSpan(), 17, 24, 0)
            it.text = content
        }

        binding.version.text = getString(
            R.string.label_version,
            requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName
        )
    }

    private fun showMessage(@StringRes strRes:Int){
        if(strRes!=0) {
            Snackbar.make(binding.recyclerView, strRes, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun save(){
        mainViewModel.saveAll(requireContext().applicationContext,binding)
    }

}