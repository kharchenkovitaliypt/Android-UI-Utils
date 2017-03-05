@file:Suppress("DEPRECATION")
package com.kharchenkovitaliypt.android.sample

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import com.kharchenkovitaliypt.android.ui.CameraFacing
import com.kharchenkovitaliypt.android.ui.Gravity.Horizontal
import com.kharchenkovitaliypt.android.ui.Gravity.Vertical
import com.kharchenkovitaliypt.android.ui.ScaleType
import com.kharchenkovitaliypt.android.ui.calculateScaleMatrix
import com.kharchenkovitaliypt.android.ui.listener.SimpleSeekBarChangeListener
import com.kharchenkovitaliypt.android.ui.openCamera

class CameraTextureViewActivity : AppCompatActivity() {

    lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = TextureView(this)
        textureView.surfaceTextureListener = listener

        val containerView = (findViewById(R.id.container) as ViewGroup)
        containerView.addView(textureView)
        textureView.layoutParams = textureView.layoutParams.apply {
            (this as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        }

        (findViewById(R.id.seekBarWidth) as SeekBar)
                .setOnSeekBarChangeListener(SimpleSeekBarChangeListener { _, progress, _ ->
            textureView.layoutParams = textureView.layoutParams.apply {
                width = (containerView.width * (progress / 100f)).toInt()
            }
        })
        (findViewById(R.id.seekBarHeight) as SeekBar)
                .setOnSeekBarChangeListener(SimpleSeekBarChangeListener { _, progress, _ ->
            textureView.layoutParams = textureView.layoutParams.apply {
                height = (containerView.height * (progress / 100f)).toInt()
            }
        })
    }

    val listener = object : SurfaceTextureListener {
        lateinit var camera: Camera

        fun updateTextureViewSize() {
            val ps = camera.parameters.previewSize

            val scaleMatrix = textureView.calculateScaleMatrix(
                    ScaleType.CROP, Vertical.TOP(Horizontal.CENTER),
                    ps.width, ps.height)
            textureView.setTransform(scaleMatrix)
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            Log.d("TAG", "onSurfaceTextureSizeChanged() surface: $width x $height")
            camera = openCamera(application, CameraFacing.FRONT)
            val ps = camera.parameters.previewSize
            Log.d("TAG", "onSurfaceTextureSizeChanged() preview: ${ps.width} x ${ps.height}")
            updateTextureViewSize()

            camera.setPreviewTexture(surface)
            camera.startPreview()

            textureView.keepScreenOn = true
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            Log.d("TAG", "onSurfaceTextureDestroyed()")
            camera.stopPreview()
            camera.release()
            textureView.keepScreenOn = false
            return true
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            Log.d("TAG", "onSurfaceTextureSizeChanged() surface: $width x $height")
            updateTextureViewSize()
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            //Log.d("TAG", "onSurfaceTextureUpdated()")
        }
    }

}
