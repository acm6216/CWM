package me.qingshu.cwm.style.adapter

import me.qingshu.cwm.style.inner.CardInnerStyleViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.data.Picture
import me.qingshu.cwm.databinding.StyleCardBinding
import me.qingshu.cwm.databinding.StyleCardInnerBinding
import me.qingshu.cwm.databinding.StyleDefaultBinding
import me.qingshu.cwm.databinding.StyleSpaceBinding
import me.qingshu.cwm.style.inner.CardInnerMarkBinding
import me.qingshu.cwm.style.card.CardPictureMarkBinding
import me.qingshu.cwm.style.card.CardStyleViewHolder
import me.qingshu.cwm.style.def.DefaultPictureMarkBinding
import me.qingshu.cwm.style.def.DefaultStyleViewHolder
import me.qingshu.cwm.style.newyear.NewYearMarkBinding
import me.qingshu.cwm.style.newyear.NewYearViewHolder
import me.qingshu.cwm.style.Styles
import me.qingshu.cwm.style.space.SpaceMarkBinding
import me.qingshu.cwm.style.space.SpaceViewHolder

class PictureAdapter(private val click: (View, Picture) -> Unit) :
    ListAdapter<Picture, RecyclerView.ViewHolder>(
        PictureListItem()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType){
        Styles.DEFAULT.ordinal -> DefaultStyleViewHolder.from(
            DefaultPictureMarkBinding(
                StyleDefaultBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
        Styles.SPACE.ordinal -> SpaceViewHolder.from(
            SpaceMarkBinding(
                StyleSpaceBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
        Styles.NEW_YEAR.ordinal -> NewYearViewHolder.from(
            NewYearMarkBinding(
                StyleDefaultBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
        Styles.INNER.ordinal -> CardInnerStyleViewHolder.from(
            CardInnerMarkBinding(
                StyleCardInnerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,false
                )
            )
        )
        else -> CardStyleViewHolder.from(
            CardPictureMarkBinding(
                StyleCardBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when(holder){
        is DefaultStyleViewHolder -> holder.bind(getItem(position), click)
        is CardStyleViewHolder -> holder.bind(getItem(position),click)
        is CardInnerStyleViewHolder -> holder.bind(getItem(position),click)
        is NewYearViewHolder -> holder.bind(getItem(position),click)
        is SpaceViewHolder -> holder.bind(getItem(position),click)
        else -> {}
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).styles.ordinal
    }
}