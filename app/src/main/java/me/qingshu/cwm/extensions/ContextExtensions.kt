package me.qingshu.cwm.extensions

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.IntegerRes
import androidx.annotation.InterpolatorRes

fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

val Context.shortAnimTime: Int
    get() = getInteger(android.R.integer.config_shortAnimTime)

fun Context.getInterpolator(@InterpolatorRes id: Int): Interpolator =
    AnimationUtils.loadInterpolator(this,id)