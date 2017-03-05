@file:JvmName("AnimationUtils")
package com.kharchenkovitaliypt.android.ui.listener

import android.animation.Animator
import android.view.animation.Animation

fun Animator.onEndListener(endListener: (Animator) -> Unit) {
    addListener(SimpleAnimatorListener(endListener))
}

fun Animation.onEndListener(endListener: (Animation) -> Unit) {
    this.setAnimationListener(SimpleAnimationListener(endListener))
}

open class SimpleAnimationListener(
        val onAnimationEndListener: ((Animation) -> Unit)? = null
): Animation.AnimationListener {

    override fun onAnimationEnd(animation: Animation) {
        onAnimationEndListener?.invoke(animation)
    }
    override fun onAnimationRepeat(animation: Animation) {}
    override fun onAnimationStart(animation: Animation) {}
}

open class SimpleAnimatorListener (
        val onAnimationEndAction: ((Animator) -> Unit)? = null
): Animator.AnimatorListener {

    override fun onAnimationEnd(animation: Animator) {
        onAnimationEndAction?.invoke(animation)
    }
    override fun onAnimationRepeat(animation: Animator) {}
    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationStart(animation: Animator) {}
}