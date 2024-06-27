package me.qingshu.cwm

import android.os.Parcelable
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.qingshu.cwm.extensions.sharedPreferences

abstract class BaseFragment:Fragment(){

    protected inline fun Fragment.repeatWithViewLifecycle(
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        if (minState == Lifecycle.State.INITIALIZED || minState == Lifecycle.State.DESTROYED) {
            throw IllegalArgumentException("minState must be between INITIALIZED and DESTROYED")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(minState) {
                block()
            }
        }
    }

    protected fun View.setOnClick(block:()->Unit){
        setOnClickListener { block.invoke() }
    }

    protected fun sharedPreferences() = requireContext().sharedPreferences()

    protected fun <V:ViewDataBinding> V.bindLifecycle(){
        lifecycleOwner = this@BaseFragment
        executePendingBindings()
    }

    protected inline val Int.dp: Int get() = run { toFloat().dp }
    protected inline val Float.dp: Int get() = run {
        val scale: Float = resources.displayMetrics.density
        (this * scale + 0.5f).toInt()
    }

    private var recyclerViewState: Parcelable? = null

    protected fun RecyclerView.storeRecyclerViewState(){
        addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = (layoutManager as LinearLayoutManager).onSaveInstanceState()
            }
        })
    }

    fun RecyclerView.store(){
        recyclerViewState = (layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

    fun RecyclerView.onRestore(){
        recyclerViewState?.also { state ->
            (layoutManager as LinearLayoutManager).onRestoreInstanceState(state)
        }
    }
}