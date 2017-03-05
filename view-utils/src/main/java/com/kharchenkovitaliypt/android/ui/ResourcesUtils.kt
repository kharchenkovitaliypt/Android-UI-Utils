@file:JvmName("ResourcesUtils")
package com.kharchenkovitaliypt.android.ui

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat

fun Resources.Theme.getResourceId(themeResId: Int, @AttrRes resAttrId: Int): Int {
    val a = obtainStyledAttributes(themeResId, intArrayOf(resAttrId))
    val attributeResourceId = a.getResourceId(0, 0)
    a.recycle()
    return attributeResourceId
}

@ColorInt
fun Resources.getCompatColor(@ColorRes colorRes: Int): Int {
    return ResourcesCompat.getColor(this, colorRes, null)
}

fun Resources.getCompatDrawable(@DrawableRes resId: Int): Drawable? {
    val drawable = ResourcesCompat.getDrawable(this, resId, null)
    // Fixed bug with NullPointerException
    if (drawable is NinePatchDrawable) {
        drawable.paint
    }
    return drawable
}