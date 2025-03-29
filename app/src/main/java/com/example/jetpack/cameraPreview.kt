package com.example.jetpack

import android.R
import android.graphics.Color
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun disp(){
    val context = LocalContext.current
    val cameraPreview_usecase = remember { androidx.camera.core.Preview.Builder().build() }
    var cameraProvider1 by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current


    AndroidView(

        modifier = Modifier,

        factory = {context ->
            PreviewView(context).also {
                cameraPreview_usecase.surfaceProvider = it.surfaceProvider
            }
        }

    )
    LaunchedEffect(Unit) {
        cameraProvider1 = ProcessCameraProvider.awaitInstance(context)
        reBindCamera(cameraProvider1,lifecycleOwner,cameraPreview_usecase)
    }
}
fun reBindCamera(
    cameraProvider: ProcessCameraProvider?,
    lifecycleOwner: LifecycleOwner,
    cameraPreviewUseCases: androidx.camera.core.Preview
){
    if(cameraProvider == null){
        return
    }
    val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
    try{
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCases
        )
    }
    catch (e: Exception) {
        e.printStackTrace()
    }







}