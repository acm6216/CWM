package me.qingshu.cwm.data

import android.content.Context
import android.graphics.Typeface

data class ArtSignature(
    val text:String,
    val visible:Boolean,
    val font:Font = Font.QuillbacksDemo
){

    fun typeface(context: Context):Typeface = Typeface.createFromAsset(
        context.assets,
        context.getString(font.fileName)
    )

    companion object{
        val empty get() = ArtSignature("",false)
    }
}