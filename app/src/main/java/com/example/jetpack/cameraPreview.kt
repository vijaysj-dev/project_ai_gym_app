package com.example.jetpack

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun camPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
){
    val life_cycle_owner = LocalLifecycleOwner.current
    AndroidView(
        factory =   {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(life_cycle_owner)
            }
        },
        modifier = Modifier
    )

}