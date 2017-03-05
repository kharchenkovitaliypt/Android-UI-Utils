package com.kharchenkovitaliypt.android.ui

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.InvocationTargetException

object SoftKeyboard {
    private val TAG = SoftKeyboard::class.java.simpleName

    fun hide(activity: Activity) {
        val focusedView = activity.currentFocus
        if(focusedView != null) {
            innerHide(focusedView.context, focusedView.windowToken)
        }
    }

    fun hide(view: View) {
        val focusedView = view.findFocus()
        if (focusedView != null) {
            innerHide(focusedView.context, focusedView.windowToken)
        }
    }

    private fun innerHide(ctx: Context, windowToken: IBinder) {
        getInputMethodManager(ctx).hideSoftInputFromWindow(windowToken, 0)
    }

    fun show(view: View) {
        view.requestFocus()
        getInputMethodManager(view.context)
                .showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun setOnShowChangedListener(activity: Activity, listener: OnShowChangedListener): Cancelable {
        return setOnShowChangedListener(activity.window.decorView, listener)
    }

    fun setOnShowChangedListener(view: View, listener: OnShowChangedListener): Cancelable {
        // Init listener
        val showChangeContext = ShowChangedContext(getInputMethodManager(view.context), listener)
        showChangeContext.notifyListener(view)

        val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener { showChangeContext.notifyListener(view) }
        view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        return ShowChangedCancelable(view, globalLayoutListener)
    }

    private class ShowChangedCancelable internal constructor(
            var view: View?,
            var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener?
    ) : Cancelable {

        override fun cancel() {
            if (view != null) {
                view!!.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
                view = null
                globalLayoutListener = null
            }
        }
    }

    private class ShowChangedContext internal constructor(internal val imm: InputMethodManager, internal val listener: OnShowChangedListener) {
        internal var shown: Boolean? = null
        internal var onAppHeight: Int = 0

        internal fun notifyListener(view: View) {
            val shown = isShown(view)
            val onAppHeight = getOnAppVisibleHeight(view)

            if (this.shown == null || this.shown != shown
                    || this.onAppHeight != onAppHeight) {
                this.shown = shown
                this.onAppHeight = onAppHeight
                listener.onShowChanged(shown, onAppHeight)

            }
        }
    }

    /**
     * @param activity for which will be checked
     * *
     * @return show state
     */
    fun isShown(activity: Activity): Boolean {
        return isShown(activity.window.decorView)
    }

    /**
     * @param view any belonging to the current window
     * *
     * @return show state
     */
    fun isShown(view: View): Boolean {
        var height: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            height = getOnDeviceVisibleHeight(view.context)
            if (height < 0) {
                height = getOnAppVisibleHeight(view)
            }
        } else {
            height = getOnAppVisibleHeight(view)
        }
        return height > 0
    }

    /**
     * @return input method window visible height occupied on current app.
     */
    fun getOnAppVisibleHeight(activity: Activity): Int {
        return getOnAppVisibleHeight(activity.window.decorView)
    }

    /**
     * @param view any belonging to the current window
     * *
     * @return input method window visible height occupied on current app.
     */
    fun getOnAppVisibleHeight(view: View): Int {
        val rootView = view.rootView as ViewGroup
        return rootView.getChildAt(0).paddingBottom
    }

    /**
     * @return input method window visible height on device or -1 if method not available.
     * *         Useful for multi window mode.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun getOnDeviceVisibleHeight(ctx: Context): Int {
        try {
            return callMethod(getInputMethodManager(ctx), "getInputMethodWindowVisibleHeight")
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            return -1
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            return -1
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return -1
        }

    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun <T> callMethod(target: Any, methodName: String): T {
        val method = target.javaClass.getDeclaredMethod(methodName)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(target) as T
    }

    fun getInputMethodManager(ctx: Context): InputMethodManager {
        return ctx.applicationContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    interface Cancelable {
        fun cancel()
    }

    interface OnShowChangedListener {
        fun onShowChanged(shown: Boolean, onAppHeight: Int)
    }
}
