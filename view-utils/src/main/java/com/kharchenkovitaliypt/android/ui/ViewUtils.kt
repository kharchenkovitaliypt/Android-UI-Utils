@file:JvmName("ViewUtils")
package com.kharchenkovitaliypt.android.ui

import android.graphics.Matrix
import android.os.Build
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView

enum class ScaleType { CROP, FIT }

sealed class Gravity {
    sealed class Vertical: Gravity() {
        class CENTER(val horizontal: Horizontal): Vertical()
        class TOP(val horizontal: Horizontal): Vertical()
        class BOTTOM(val horizontal: Horizontal): Vertical()
    }
    enum class Horizontal { CENTER, LEFT, RIGHT }
}

fun View.calculateScaleMatrix(
        scaleType: ScaleType, gravity: Gravity,
        contentWidth: Int, contentHeight: Int,
        outMatrix: Matrix = Matrix()
): Matrix {
    val portrait = isVerticalDisplay(context)
    val contentW = if(portrait) contentHeight else contentWidth
    val contentH = if(portrait) contentWidth else contentHeight
    val (scaleWidth, scaleHeight) = calculateScale(
            contentW.toFloat(), contentH.toFloat(), scaleType)

    return outMatrix.apply {
        val (pivotX, pivotY) = getPivotPoint(gravity)
        setScale(scaleWidth, scaleHeight, pivotX.toFloat(), pivotY.toFloat())
    }
}

fun View.calculateScale(
        contentWidth: Float, contentHeight: Float,
        scaleType: ScaleType
): Pair<Float, Float> {
    if(width <= 0 || height <=0) return 0f to 0f

    var scaleWidth = 1.0f
    var scaleHeight = 1.0f
    val containerRatio = width / height
    val viewRatio = contentWidth / contentHeight

    if(scaleType == ScaleType.CROP) {
        if (viewRatio > containerRatio) {
            scaleWidth = (contentWidth / width) / (contentHeight / height)
        } else {
            scaleHeight = (contentHeight / height) / (contentWidth / width)
        }
    } else {
        // Fit
        if (viewRatio > containerRatio) {
            scaleHeight = (contentHeight / height) / (contentWidth / width)
        } else {
            scaleWidth = (contentWidth / width) / (contentHeight / height)
        }
    }
    return scaleWidth to scaleHeight
}

fun View.getPivotPoint(gravity: Gravity): Pair<Int, Int> {
    val pivotX: Int
    val pivotY: Int
    when (gravity) {
        is Gravity.Vertical.TOP -> {
            pivotX = when(gravity.horizontal) {
                Gravity.Horizontal.LEFT -> 0
                Gravity.Horizontal.RIGHT -> width
                Gravity.Horizontal.CENTER -> width / 2
            }
            pivotY = 0
        }
        is Gravity.Vertical.BOTTOM -> {
            pivotX = when(gravity.horizontal) {
                Gravity.Horizontal.LEFT -> 0
                Gravity.Horizontal.RIGHT -> width
                Gravity.Horizontal.CENTER -> width / 2
            }
            pivotY = height
        }
        is Gravity.Vertical.CENTER -> {
            pivotX = when(gravity.horizontal) {
                Gravity.Horizontal.LEFT -> 0
                Gravity.Horizontal.RIGHT -> width
                Gravity.Horizontal.CENTER -> width / 2
            }
            pivotY = height / 2
        }
    }
    return pivotX to pivotY
}

fun View.onLaidOut(action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (ViewCompat.isLaidOut(this@onLaidOut)) {
                viewTreeObserver.removeOnGlobalOnLayoutListener(this)
                action()
            }
        }
    })
}

fun ViewTreeObserver.removeOnGlobalOnLayoutListener(victim: ViewTreeObserver.OnGlobalLayoutListener) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        removeOnGlobalLayoutListener(victim)
    } else {
        @Suppress("DEPRECATION")
        removeGlobalOnLayoutListener(victim)
    }
}

fun TextView.setTextOrHide(charSequence: CharSequence?, hideMode: Int = View.GONE) {
    if (charSequence.isNullOrBlank()) {
        text = ""
        visibility = hideMode
    } else {
        text = charSequence
        visibility = View.VISIBLE
    }
}
