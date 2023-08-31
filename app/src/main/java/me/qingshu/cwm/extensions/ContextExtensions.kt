package me.qingshu.cwm.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.InterpolatorRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.TintTypedArray
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

val Context.shortAnimTime: Int
    get() = getInteger(android.R.integer.config_shortAnimTime)

fun Context.sharedPreferences(): SharedPreferences = defaultSharedPreferences(this)

fun Context.getInterpolator(@InterpolatorRes id: Int): Interpolator =
    AnimationUtils.loadInterpolator(this,id)

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable =
    AppCompatResources.getDrawable(this, id)!!

@SuppressLint("RestrictedApi")
fun Context.getColorStateListByAttr(@AttrRes attr: Int): ColorStateList =
    obtainStyledAttributesCompat(attrs = intArrayOf(attr)).use { it.getColorStateList(0) }

@SuppressLint("RestrictedApi")
fun Context.obtainStyledAttributesCompat(
    set: AttributeSet? = null,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
): TintTypedArray =
    TintTypedArray.obtainStyledAttributes(this, set, attrs, defStyleAttr, defStyleRes)

@OptIn(ExperimentalContracts::class)
@SuppressLint("RestrictedApi")
inline fun <R> TintTypedArray.use(block: (TintTypedArray) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block(this)
    } finally {
        recycle()
    }
}