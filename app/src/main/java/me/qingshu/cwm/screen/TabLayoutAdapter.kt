package me.qingshu.cwm.screen

import android.util.DisplayMetrics
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.qingshu.cwm.R
import me.qingshu.cwm.app
import me.qingshu.cwm.databinding.TabLayoutItemBinding

class TabLayoutAdapter(
    private val click:(ScreenTab,Int,Int,TabLayoutAdapter)->Unit
):RecyclerView.Adapter<TabLayoutViewHolder>() {

    var checkPosition = 1
        set(value) {
            if(field>0)
                notifyItemChanged(field)
            notifyItemChanged(value)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabLayoutViewHolder
        = TabLayoutViewHolder.from(parent)

    override fun getItemCount(): Int = ScreenTab.values().size + 2

    override fun onBindViewHolder(holder: TabLayoutViewHolder, position: Int) {
        val item = when(position){
            0,ScreenTab.values().size+1 -> null
            else -> ScreenTab.values()[position-1]
        }
        holder.bind(item,click,this,checkPosition==position)
    }

    fun isVertical():Boolean{
        return app.resources.getBoolean(R.bool.is_vertical)
    }
}

class TabLayoutViewHolder(
    private val binding:TabLayoutItemBinding
):RecyclerView.ViewHolder(binding.root){

    val view get() = binding

    fun bind(
        screenTab: ScreenTab?,
        click: (ScreenTab, Int, Int, TabLayoutAdapter) -> Unit,
        adapter: TabLayoutAdapter,
        isCheck:Boolean
    ) {

        if (screenTab != null) {
            binding.icon.setImageResource(screenTab.icon)
            binding.label.setText(screenTab.label)
            binding.check.isChecked = isCheck
            binding.root.setOnClickListener {
                val location = IntArray(2)
                binding.icon.getLocationOnScreen(location)
                click.invoke(screenTab, location[0], location[1], adapter)
            }
            binding.check.setOnClickListener {
                val location = IntArray(2)
                binding.icon.getLocationOnScreen(location)
                click.invoke(screenTab, location[0], location[1], adapter)
            }

            if (!adapter.isVertical()) binding.root.minimumWidth = 0
            else binding.root.minimumHeight = 48f.dp + 12f.sp + 16.dp
            binding.root.visibility = View.VISIBLE
        } else {
            binding.root.visibility = View.INVISIBLE
            if (!adapter.isVertical()) {
                binding.root.minimumWidth = (getScreenWidth() / 2f - 36f.dp - 6f.sp).toInt()
                binding.root.minimumHeight = 0
            } else {
                binding.root.minimumHeight =
                    (getScreenHeight() / 2f - 48f.dp - 12f.sp).toInt()
                binding.root.minimumWidth = 0
            }
        }
    }

    private inline val Float.sp: Int get() = run {
        applyDimension(
            COMPLEX_UNIT_SP,
            this,
            getDisplayMetrics()
        ).toInt()
    }


    private inline val Int.dp: Int get() = run { toFloat().dp }
    private inline val Float.dp: Int get() = run {
        val scale: Float = binding.root.resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        return binding.root.context.resources.displayMetrics
    }

    private fun getScreenWidth(): Int {
        return getDisplayMetrics().widthPixels
    }

    private fun getScreenHeight(): Int {
        return getDisplayMetrics().heightPixels-24.dp
    }

    companion object{
        fun from(parent:ViewGroup):TabLayoutViewHolder{
            return TabLayoutViewHolder(
                TabLayoutItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )
        }
    }
}