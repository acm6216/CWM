package me.qingshu.cwm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.data.ExifInformation
import me.qingshu.cwm.databinding.ExifItemBinding

class ExifDetailAdapter:ListAdapter<ExifInformation,ExifViewHolder>(
    ExifItemCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifViewHolder = ExifViewHolder.from(parent)

    override fun onBindViewHolder(holder: ExifViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class ExifViewHolder(
    private val binding:ExifItemBinding
):RecyclerView.ViewHolder(binding.root){

    fun bind(exifInformation: ExifInformation){
        binding.exif = exifInformation
        binding.executePendingBindings()
    }

    companion object{

        fun from(parent:ViewGroup):ExifViewHolder = ExifViewHolder(
            ExifItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }
}

class ExifItemCallback: DiffUtil.ItemCallback<ExifInformation>(){
    override fun areItemsTheSame(oldItem: ExifInformation, newItem: ExifInformation): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ExifInformation, newItem: ExifInformation): Boolean {
        return oldItem==newItem
    }

}