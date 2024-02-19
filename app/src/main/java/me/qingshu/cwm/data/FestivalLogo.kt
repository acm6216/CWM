package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class FestivalLogo(
    @StringRes override val label: Int,
    @DrawableRes override val src: Int,
    override val tintEnable: Boolean = true,
    override val padding: Int = 12
):Icon {
    CHINESE_NEW_YEAR(R.string.festival_label_long,R.drawable.tz_long,true,-8),
    CHINESE_NEW_YEAR2(R.string.festival_label_long,R.drawable.tz_long_1,false,-12),
    CHINESE_NEW_YEAR3(R.string.festival_label_long,R.drawable.tz_long_2,true,8),
    CHINESE_NEW_YEAR4(R.string.festival_label_long,R.drawable.tz_long_3,false,8);

    override fun getNumberOfIcons(): Array<out Icon> = FestivalLogo.values()
}