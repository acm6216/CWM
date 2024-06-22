package me.qingshu.cwm.style.adapter

import me.qingshu.cwm.style.inner.CardInnerStyleViewHolder
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.style.card.CardStyleViewHolder
import me.qingshu.cwm.style.def.DefaultStyleViewHolder
import me.qingshu.cwm.style.Styles
import me.qingshu.cwm.style.space.SpaceViewHolder

class PictureAdapter(
    private val click: (View, Picture) -> Unit
) : ListAdapter<Picture, RecyclerView.ViewHolder>(
    PictureListItem()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType){
        Styles.DEFAULT.ordinal -> DefaultStyleViewHolder.from(parent)
        Styles.SPACE.ordinal -> SpaceViewHolder.from(parent)
        Styles.INNER.ordinal -> CardInnerStyleViewHolder.from(parent)
        else -> CardStyleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when(holder){
        is DefaultStyleViewHolder -> holder.bind(getItem(position), click)
        is CardStyleViewHolder -> holder.bind(getItem(position),click)
        is CardInnerStyleViewHolder -> holder.bind(getItem(position),click)
        is SpaceViewHolder -> holder.bind(getItem(position),click)
        else -> {}
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).styles.ordinal
    }
}