package com.kharchenkovitaliypt.android.ui.listener

import android.widget.SeekBar

open class SimpleSeekBarChangeListener(
        val onProgressChangedListener: ((SeekBar, Int, Boolean) -> Unit)? = null
) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        onProgressChangedListener?.invoke(seekBar, progress, fromUser)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}