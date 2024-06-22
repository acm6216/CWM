package me.qingshu.cwm.data

import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class Font(
    @StringRes val label:Int,
    @StringRes val fileName:Int
){
    QuillbacksDemo(R.string.font_label_quillbacks_demo,R.string.font_file_quillbacks_demo),
    Arkipelago(R.string.font_label_arkipelago,R.string.font_file_arkipelago),
    Modesty(R.string.font_label_modesty,R.string.font_file_modesty),
    Bernd(R.string.font_label_bernd,R.string.font_file_bernd),
    Autograf(R.string.font_label_autograf,R.string.font_file_autograf)

}