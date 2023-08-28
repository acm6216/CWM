package me.qingshu.cwm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.PictureMarkBinding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PictureItemBinding
import me.qingshu.cwm.databinding.PreviewBinding

class Preview : BaseFragment() {

    private val binding by lazy { PreviewBinding.inflate(layoutInflater) }

    private val picture: PictureViewModel by activityViewModels()

    private val pictureAdapter = PictureAdapter { view,pic ->
        PopupMenu(view.context,view).apply {
            menuInflater.inflate(R.menu.option_menu,menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_remove -> picture.removePicture(pic)
                    else -> picture.removeAllPicture()
                }
                true
            }
            show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = picture
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        binding.save.setOnClickListener(::save)
        binding.recyclerView.adapter = pictureAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        repeatWithViewLifecycle {
            launch {
                picture.previewPictures.collect {
                    pictureAdapter.submitList(it)
                }
            }
        }

    }

    /*private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { state ->
        if(state) picture.save(binding.root.context)
    }*/

    private fun save(view: View){
        SaveStatusDialog().also {
            //it.show(childFragmentManager, javaClass.simpleName)
            picture.save(view.context,binding.pictureItem){
                //it.dismiss()
            }
        }
        //requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    class PictureAdapter(private val click: (View,Picture) -> Unit) :
        ListAdapter<Picture, RecyclerView.ViewHolder>(
            PictureListItem()
        ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            PictureViewHolder.from(PictureMarkBinding(PictureItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)))

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