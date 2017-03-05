@file:JvmName("DisplayUtils")
package com.kharchenkovitaliypt.android.ui

import android.content.Context
import android.view.Surface
import android.view.WindowManager

fun isVerticalDisplay(ctx: Context): Boolean {
    val orientation = getDisplayOrientation(ctx)
    return orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180
}

fun getDisplayOrientation(ctx: Context): Int {
    val windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.rotation
}
