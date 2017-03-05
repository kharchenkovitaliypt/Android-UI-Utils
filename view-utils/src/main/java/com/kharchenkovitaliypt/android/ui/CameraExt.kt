@file:JvmName("CameraUtils")
@file:Suppress("DEPRECATION")
package com.kharchenkovitaliypt.android.ui

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK
import android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
import android.view.Surface

enum class CameraFacing(val value: Int) {
    ANY(-1),
    BACK(CAMERA_FACING_BACK),
    FRONT(CAMERA_FACING_FRONT)
}

fun isCameraAvailable(facing: CameraFacing): Boolean = getCameraId(facing) != null

fun openCamera(ctx: Context, facing: CameraFacing = CameraFacing.ANY): Camera {
    val id = getCameraId(facing)
    id ?: throw RuntimeException("No found camera: $facing")
    return openCamera(ctx, id)
}

private fun getCameraId(facing: CameraFacing): Int? {
    val numberOfCameras = Camera.getNumberOfCameras()
    val cameraInfo = Camera.CameraInfo()
    for (id in 0..numberOfCameras - 1) {
        Camera.getCameraInfo(id, cameraInfo)
        if(facing == CameraFacing.ANY || cameraInfo.facing == facing.value) {
            return id
        }
    }
    return null
}

private fun openCamera(ctx: Context, id: Int): Camera {
    return Camera.open(id).apply { fixCameraOrientation(ctx, this, id) }
}

private fun fixCameraOrientation(ctx: Context, camera: Camera, id: Int) {
    val info = Camera.CameraInfo()
    Camera.getCameraInfo(id, info)
    var degrees = 0

    when (getDisplayOrientation(ctx)) {
        Surface.ROTATION_0 -> degrees = 0
        Surface.ROTATION_90 -> degrees = 90
        Surface.ROTATION_180 -> degrees = 180
        Surface.ROTATION_270 -> degrees = 270
    }

    var result: Int
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        result = (info.orientation + degrees) % 360
        result = (360 - result) % 360
    } else {
        result = (info.orientation - degrees + 360) % 360
    }

    camera.setDisplayOrientation(result)
}
