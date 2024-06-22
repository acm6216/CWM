package me.qingshu.cwm.screen.adapter

import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.databinding.SingleChoiceItemBinding

abstract class PickerAdapter<T:RecyclerView.ViewHolder,K:Any>(
    private val create:(ViewGroup)->T,
    private val bind:(T, K, Int, Boolean)->Unit,
    temCallback: DiffUtil.ItemCallback<K>,
    private val checkable:Boolean = false
): ListAdapter<K, T>(
    temCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T = create(parent)

    override fun onBindViewHolder(holder: T, position: Int) {
        bind(holder,getItem(position),position,checkable)
    }

}
