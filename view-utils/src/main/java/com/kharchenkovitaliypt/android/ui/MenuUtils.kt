@file:JvmName("MenuUtils")
package com.kharchenkovitaliypt.android.ui

import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.MenuItem

//fun MenuItem.setIconTintResource(ctx: Context, @ColorRes colorRes: Int) {
//    setIconTint(ContextCompat.getColor(ctx, colorRes))
//}

fun MenuItem.setIconTint(@ColorInt color: Int) {
    val iconDrawable = DrawableCompat.wrap(icon)
    DrawableCompat.setTint(iconDrawable, color)
    icon = iconDrawable
}