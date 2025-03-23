package com.example.jetpack

import android.Manifest  // ✅ Import needed for CAMERA permission
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class CamPermission1 {
    companion object {
        val camPer = arrayOf(Manifest.permission.CAMERA)
    }

    fun permission_check(context: Context): Boolean{
        return camPer.all {
            ContextCompat.checkSelfPermission(
                context,it

            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
