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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.PictureMarkBinding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PictureItemBinding
import me.qingshu.cwm.databinding.PreviewBinding
import me.qingshu.cwm.extensions.fadeToVisibilityUnsafe

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
                    R.id.menu_lens_param -> viewModel.useLens(picture)
                    R.id.menu_date_param -> viewModel.useDate(picture)
                    R.id.menu_device_param -> viewModel.useDevice(picture)
                    R.id.menu_location_param -> viewModel.useLocation(picture)
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
        binding.save.setOnClickListener(::save)
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

    private fun save(view: View){
        viewModel.save(view.context,binding.pictureItem)
    }

    class PictureAdapter(private val click: (View,Picture) -> Unit) :
        ListAdapter<Picture, RecyclerView.ViewHolder>(
            PictureListItem()
        ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            PictureViewHolder.from(
                PictureMarkBinding(
                    PictureItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            )

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            (holder as PictureViewHolder).bind(getItem(position), click)
    }

    class PictureListItem : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem.uri.toString() == newItem.uri.toString()
        }

    }

    class PictureViewHolder private constructor(
        private val binding: PictureMarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture, click: (View,Picture) -> Unit) {
            binding.setMark(picture,click = click)
        }

        companion object {
            fun from(binding: PictureMarkBinding) = PictureViewHolder(binding)
        }
    }

}