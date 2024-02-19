package me.qingshu.cwm.binding

import android.content.Context
import androidx.viewbinding.ViewBinding
import me.qingshu.cwm.databinding.ParamBinding

abstract class Binding<T:ViewBinding>(private val paramBinding: ParamBinding) {
    abstract val binding:T

    protected val context: Context get() = binding.root.context

    protected fun get(block:((ParamBinding)->T)):T{
        return block.invoke(paramBinding)
    }
}