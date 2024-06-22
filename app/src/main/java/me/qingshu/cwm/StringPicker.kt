package me.qingshu.cwm

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.databinding.StringItemBinding
import me.qingshu.cwm.databinding.StringPickerBinding

class StringPicker(
    private val array:Array<String>,
    private val auto:String,
    @StringRes private val title:Int,
    private val picker:((String)->Unit)?=null
):DialogFragment() {

    private val binding: StringPickerBinding by lazy { StringPickerBinding.inflate(layoutInflater) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = MaterialAlertDialogBuilder(requireContext())
        .setView(binding.root).setTitle(title).create().also {
            onCreated()
        }

    private fun onCreated() {
        binding.recyclerView.apply {
            adapter = SymbolAdapter(auto,array){
                picker?.invoke(it)
                dismiss()
            }
            layoutManager = GridLayoutManager(binding.root.context, 5)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scrollIndicators = View.SCROLL_INDICATOR_BOTTOM or View.SCROLL_INDICATOR_TOP
            }
        }
    }

    class SymbolAdapter(
        private val auto:String,
        private val symbols: Array<String>,
        private val click:(String)->Unit
    ): RecyclerView.Adapter<SymbolViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder
                = SymbolViewHolder.from(StringItemBinding.inflate(LayoutInflater.from(parent.context)))

        override fun getItemCount(): Int = symbols.size

        override fun onBindViewHolder(holder: SymbolViewHolder, position: Int)
                = holder.bind(auto,symbols[position],position,click)
    }

    class SymbolViewHolder(private val binding: StringItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(auto: String,char: String,position: Int, click: (String) -> Unit) = binding.apply {
            symbol.setOnClickListener {
                click.invoke(
                    when(position) {
                        0 -> auto
                        else -> char
                    }
                )
            }
            symbol.text = char
            symbol.contentDescription = char
        }.executePendingBindings()

        companion object {
            fun from(binding: StringItemBinding) = SymbolViewHolder(binding)
        }
    }
}