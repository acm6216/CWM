package me.qingshu.cwm.screen.adapter

import androidx.recyclerview.widget.DiffUtil

class CheckableItem<T:CheckableItemCallback>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isCheck == newItem.isCheck
    }

}