package me.qingshu.cwm.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.qingshu.cwm.R

enum class Logo(
    @StringRes val label: Int,
    @DrawableRes val src: Int,
    val tintEnable: Boolean = true,
    val padding: Int = 12
) {
    CANNON(R.string.label_canon, R.drawable.logo_canon, tintEnable = false),
    SONY_ALPHA(R.string.label_sony, R.drawable.logo_sony_alpha, tintEnable = false),
    SONY(R.string.label_sony, R.drawable.logo_sony, padding = -8),
    FUJIFILM(R.string.label_fujifilm, R.drawable.logo_fujifilm, padding = 16),
    PANASONIC(
        R.string.label_panasonic,
        R.drawable.logo_panasonic,
        padding = -16,
        tintEnable = false
    ),
    NIKON(R.string.label_nikon, R.drawable.logo_nikon, padding = 0),
    NIKON_NEW(R.string.label_nikon, R.drawable.logo_nikon_new, padding = -2, tintEnable = false),
    OLYMPUS(R.string.label_olympus, R.drawable.logo_olympus, padding = -16),
    LEICA(R.string.label_leica, R.drawable.logo_leica, tintEnable = false, padding = -8),
    LEICA_NEW(R.string.label_leica, R.drawable.logo_leica_new, padding = -4),
    HASSELBLAD(R.string.label_hasselblad, R.drawable.logo_hasselblad, padding = 0),
    SIGMA(R.string.label_sigma, R.drawable.logo_sigma, padding = 16),
    LUMIX(R.string.label_lumix, R.drawable.logo_lumix, padding = -16),
    APPLE(R.string.label_apple, R.drawable.logo_apple),
    APPLE_COLORFUL(R.string.label_apple, R.drawable.logo_apple_colorful, tintEnable = false),
    MEIZU(R.string.label_meizu, R.drawable.logo_meizu, tintEnable = false),
    HONOR(R.string.label_honor, R.drawable.logo_honor),
    DJI(R.string.label_dji, R.drawable.logo_dji, padding = 16),
    INSTA360(R.string.label_insta360, R.drawable.logo_insta360),
    SAMSUNG(R.string.label_samsung, R.drawable.logo_samsung, tintEnable = false),
    SHARP(R.string.label_sharp, R.drawable.logo_sharp, tintEnable = false, padding = -8),
    RICOH(R.string.label_ricoh, R.drawable.logo_ricoh, tintEnable = false, padding = -8),
    LG(R.string.logitech_gaming, R.drawable.logo_logitech_gaming, tintEnable = false),
    LG_NEW(R.string.logitech_gaming, R.drawable.logo_logitech_gaming_new, tintEnable = false),
    GOOGLE(R.string.label_google, R.drawable.logo_google, tintEnable = false),
    LENOVO(R.string.label_lenovo, R.drawable.logo_lenovo, tintEnable = false),
    REALME(R.string.label_realme, R.drawable.logo_realme),
    MOTOROLA(R.string.label_motorola, R.drawable.logo_motorola, padding = -8),
    XIAOMI(R.string.label_xiaomi, R.drawable.logo_xiaomi, tintEnable = false),
    HUAWEI(R.string.label_huawei, R.drawable.logo_huawei),
    HUAWEI_NEW(R.string.label_huawei, R.drawable.logo_huawei_new, tintEnable = false),
    HTC(R.string.label_htc, R.drawable.logo_htc, tintEnable = false, padding = 16),
    VIVO(R.string.label_vivo, R.drawable.logo_vivo, tintEnable = false),
    ONE_PLUS(R.string.label_one_plus, R.drawable.logo_oneplus, tintEnable = false),
    ONE_PLUS_NEW(R.string.label_one_plus, R.drawable.logo_oneplus_new, tintEnable = false),
    MICROSOFT(R.string.label_microsoft, R.drawable.logo_microsoft, tintEnable = false),
    NOKIA(R.string.label_nokia, R.drawable.logo_nokia, tintEnable = false),
    ZEISS(R.string.label_zeiss, R.drawable.logo_zeiss, tintEnable = false),

    LG_ONE(R.string.logitech_gaming, R.drawable.logo_logitech_gaming_one, tintEnable = true, padding = 8),
    OPPO(R.string.label_oppo, R.drawable.logo_oppo, tintEnable = false),
    MICROSOFT_ONE(R.string.label_microsoft, R.drawable.logo_microsoft_one, tintEnable = false),
    NUBIA(R.string.label_nubia, R.drawable.logo_nubia, tintEnable = false, padding = 16),
    XPERIA(R.string.label_sony, R.drawable.logo_xperia),
    MASTER(R.string.label_master, R.drawable.logo_master, tintEnable = false),
    GOOGLE_ONE(R.string.label_google, R.drawable.logo_google_one, tintEnable = false, padding = 12),
    HUAWEI_ONE(R.string.label_huawei, R.drawable.logo_huawei_one, tintEnable = false),
    SMARTISAN(R.string.label_smartisan, R.drawable.logo_smartisan, tintEnable = false),
    LEICA_ONE(R.string.label_leica, R.drawable.logo_leica_one, tintEnable = false, padding = 16),
    HASSELBLAD_ONE(R.string.label_hasselblad, R.drawable.logo_hasselblad_two, padding = 0),
    HASSELBLAD_TWO(R.string.label_hasselblad, R.drawable.logo_hasselblad_one, padding = 16),
    SAMSUNG_ONE(R.string.label_samsung, R.drawable.logo_samsung_one, tintEnable = true),
    MOTOROLA_NEW(R.string.label_motorola, R.drawable.logo_motorola_new, padding = 8);

    fun compatPadding() = if (padding >= 16) padding / 2 else if (padding >= 12) 0 else padding

    fun iconPadding() = when (this) {
        DJI, FUJIFILM, LEICA_ONE, GOOGLE_ONE, NUBIA, HTC -> padding / 4
        PANASONIC, OLYMPUS, SIGMA, LUMIX, MOTOROLA_NEW, LG_ONE -> padding / 2
        NIKON -> -2
        else -> compatPadding()
    }
}