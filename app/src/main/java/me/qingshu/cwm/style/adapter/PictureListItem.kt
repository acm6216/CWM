package me.qingshu.cwm.style.adapter

import androidx.recyclerview.widget.DiffUtil
import me.qingshu.cwm.data.Picture

class PictureListItem : DiffUtil.ItemCallback<Picture>() {
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.uri.toString() == newItem.uri.toString()
    }

}