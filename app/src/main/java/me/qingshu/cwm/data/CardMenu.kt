package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class CardMenu(
    @DrawableRes val icon:Int,
    @StringRes val label:Int
) {
    PICKER(R.drawable.ic_picture,R.string.screen_picture_picker),
    EXIF(R.drawable.ic_exif,R.string.screen_picture_exif),
    SAVE(R.drawable.ic_save,R.string.screen_picture_save);
}