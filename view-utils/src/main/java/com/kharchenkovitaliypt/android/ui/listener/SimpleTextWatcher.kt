package com.kharchenkovitaliypt.android.ui.listener

import android.text.Editable
import android.text.TextWatcher

open class SimpleTextWatcher (
        val onTextChangedListener: ((CharSequence, Int, Int, Int) -> Unit)? = null
): TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        onTextChangedListener?.invoke(s, start, count, after)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}
