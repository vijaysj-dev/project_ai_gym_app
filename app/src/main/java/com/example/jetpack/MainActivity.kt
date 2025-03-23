package com.example.jetpack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.jetpack.ui.theme.JetpackTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (!CamPermission.permission_check(this)) {
            ActivityCompat.requestPermissions(this, CamPermission.camPer, 0)
        } else {
            initializeCam()
        }
    }

    private fun initializeCam() {
        setContent {
            val context = LocalContext.current
            val lifecycleOwner = LocalContext.current as LifecycleOwner

            JetpackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        CameraPreview(lifecycleOwner)
                    }
                }
            }
        }
    }

    @Composable
    fun CameraPreview(lifecycleOwner: LifecycleOwner) {
        val context = LocalContext.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val previewView = remember { PreviewView(context) }

        LaunchedEffect(cameraProviderFuture) {
            val provider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

            try {
                provider.unbindAll()
                camera = provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("Camera", "Use case binding failed", e)
            }
        }

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeCam()
        } else {
            Log.e("Camera", "Permission denied")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

object CamPermission {
    val camPer = arrayOf(Manifest.permission.CAMERA)

    fun permission_check(context: ComponentActivity): Boolean {
        return camPer.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
