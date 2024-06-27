package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class PickerAdapter<T:RecyclerView.ViewHolder,K:Any>(
    private val create:(ViewGroup)->T,
    private val bind:(T, K, Int)->Unit,
    itemCallback: DiffUtil.ItemCallback<K>
): ListAdapter<K, T>(
    itemCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T = create(parent)

    override fun onBindViewHolder(holder: T, position: Int) {
        bind(holder,getItem(position),position)
    }

}
