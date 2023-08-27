package me.qingshu.cwm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.qingshu.cwm.binding.PictureMarkBinding
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.PictureItemBinding
import me.qingshu.cwm.databinding.PreviewBinding

class Preview : Fragment() {

    private val binding by lazy { PreviewBinding.inflate(layoutInflater) }

    private val picture: PictureViewModel by activityViewModels()

    private val pictureAdapter = PictureAdapter {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val dialog = SaveStatusDialog().also {
            it.show(childFragmentManager, javaClass.simpleName)
        }
        picture.save(view.context,binding.pictureItem){
            dialog.dismiss()
        }
        //requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private inline fun Fragment.repeatWithViewLifecycle(
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        if (minState == Lifecycle.State.INITIALIZED || minState == Lifecycle.State.DESTROYED) {
            throw IllegalArgumentException("minState must be between INITIALIZED and DESTROYED")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(minState) {
                block()
            }
        }
    }

    class PictureAdapter(private val click: () -> Unit) :
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

        fun bind(picture: Picture, click: () -> Unit) {
            binding.setMark(picture,click = click)
        }

        companion object {
            fun from(binding: PictureMarkBinding) = PictureViewHolder(binding)
        }
    }

}