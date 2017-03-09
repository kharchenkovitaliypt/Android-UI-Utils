package com.kharchenkovitaliypt.android.ui.listener

import android.view.View
import android.view.View.OnClickListener

/**
 * Click listener for prevent rapid click.
 * One time in @THRESHOLD ms
 */

private val THRESHOLD: Long = 350

class ParkinsonClickListener(private val clickListener: OnClickListener) : OnClickListener {

    constructor(clickListener: (View) -> Unit): this(OnClickListener { clickListener.invoke(it) })

    private var currentTime: Long = 0

    override fun onClick(view: View) {
        if (System.currentTimeMillis() - currentTime > THRESHOLD) {
            clickListener.onClick(view)
        }
        currentTime = System.currentTimeMillis()
    }
}
