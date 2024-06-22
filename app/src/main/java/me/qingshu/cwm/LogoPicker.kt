package me.qingshu.cwm

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.qingshu.cwm.data.Logo
import me.qingshu.cwm.databinding.LogoItemBinding
import me.qingshu.cwm.databinding.LogoPickerBinding

class LogoPicker(
    private val icons: Logo,
    private val logoPicker:((Logo)->Unit)?=null
):DialogFragment() {

    private val binding:LogoPickerBinding by lazy { LogoPickerBinding.inflate(layoutInflater) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        = MaterialAlertDialogBuilder(requireContext())
        .setView(binding.root).setTitle(R.string.logo_picker_title).create().also {
            onCreated()
        }

    private fun onCreated() {
        binding.recyclerView.apply {
            adapter = LogoAdapter(icons){
                logoPicker?.invoke(it)
                dismiss()
            }
            layoutManager = GridLayoutManager(binding.root.context, 3)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scrollIndicators = View.SCROLL_INDICATOR_BOTTOM or View.SCROLL_INDICATOR_TOP
            }
        }
    }

    class LogoAdapter(private val icons: Logo,private val click:(Logo)->Unit):RecyclerView.Adapter<LogoViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogoViewHolder
            = LogoViewHolder.from(LogoItemBinding.inflate(LayoutInflater.from(parent.context)))

        override fun getItemCount(): Int = icons.getNumberOfIcons().size

        override fun onBindViewHolder(holder: LogoViewHolder, position: Int)
            = holder.bind(icons.getNumberOfIcons()[position],click)
    }

    class LogoViewHolder(private val binding: LogoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private inline val Int.dp: Int get() = run { toFloat().dp }
        private inline val Float.dp: Int
            get() = run {
                val scale: Float = binding.root.resources.displayMetrics.density
                (this * scale + 0.5f).toInt()
            }

        fun bind(icon: Logo, click: (Logo) -> Unit) = binding.apply {
            src.setOnClickListener {
                click.invoke(icon)
            }
            src.setImageResource(icon.src)
            src.contentDescription = root.context.getString(icon.label)
            src.setPadding(icon.padding.dp)
        }.executePendingBindings()

        companion object {
            fun from(binding: LogoItemBinding) = LogoViewHolder(binding)
        }
    }
}