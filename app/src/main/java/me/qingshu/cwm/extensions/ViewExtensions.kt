package me.qingshu.cwm.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import androidx.core.view.*
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*
import kotlin.coroutines.resume

fun Chip.cornel() = if(isChecked) 1 else 0
fun Chip.shadow() = if(isChecked) 1 shl 1 else 0

fun View.setOnClickListener(block:()->Unit) = setOnClickListener { block.invoke() }

@OptIn(DelicateCoroutinesApi::class)
fun View.fadeToVisibilityUnsafe(visible: Boolean, force: Boolean = false, gone: Boolean = false) {
    GlobalScope.launch(Dispatchers.Main.immediate) { fadeToVisibility(visible, force, gone) }
}

suspend fun View.fadeToVisibility(visible: Boolean, force: Boolean = false, gone: Boolean = false) {
    if (visible) {
        fadeIn(force)
    } else {
        fadeOut(force, gone)
    }
}

suspend fun View.fadeIn(force: Boolean = false) {
    if (!isVisible) {
        alpha = 0f
        isVisible = true
    }
    animate().run {
        alpha(1f)
        if (!(isLaidOut || force) || (isVisible && alpha == 1f)) {
            duration = 0
        } else {
            duration = context.shortAnimTime.toLong()
            interpolator = context.getInterpolator(android.R.interpolator.fast_out_slow_in)
        }
        start()
        awaitEnd()
    }
}

suspend fun View.fadeOut(force: Boolean = false, gone: Boolean = false) {
    animate().run {
        alpha(0f)
        if (!(isLaidOut || force) || (!isVisible || alpha == 0f)) {
            duration = 0
        } else {
            duration = context.shortAnimTime.toLong()
            interpolator = context.getInterpolator(android.R.interpolator.fast_out_linear_in)
        }
        start()
        awaitEnd()
    }
    if (gone) {
        isGone = true
    } else {
        isInvisible = true
    }
}

suspend fun ViewPropertyAnimator.awaitEnd(): Unit =
    suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation { cancel() }
        setListener(object : AnimatorListenerAdapter() {
            private var canceled = false

            override fun onAnimationCancel(animation: Animator) {
                canceled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                setListener(null)
                if (continuation.isActive) {
                    if (canceled) {
                        continuation.cancel()
                    } else {
                        continuation.resume(Unit)
                    }
                }
            }
        })
    }

 fun <T:View> T.treeObserver(block:((T)->Unit)) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            block.invoke(this@treeObserver)
            this@treeObserver.viewTreeObserver.removeOnPreDrawListener(this)
            return true
        }
    })
}