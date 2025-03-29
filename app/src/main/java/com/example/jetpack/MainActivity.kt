package com.example.jetpack

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp  //
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack.ui.theme.JetpackTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //camera_activity()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Only request for API 23+
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "CAMERA permission NOT granted, requesting now...")
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001)
                } else {
                    Log.d("Permission", "CAMERA permission already granted!")
                }
            }
            JetpackTheme {
                val navController = rememberNavController()
                appNavHost(navController)


            }



        }
    }
}



@Composable
fun appNavHost(navController: NavHostController){
    NavHost(navController = navController, startDestination = "home"){
        composable("home") {Disp("WELCOME",navController)  }
        composable("cameraPreview") {disp()}
    }
}

@Composable
fun Disp(name: String, navController: NavController) {
    var message by remember { mutableStateOf(name) }
    var changed_ by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
            .background(brush = Brush.linearGradient(listOf(Color.Cyan, Color.Yellow, Color.Red)))

    ) {
        Box(
            modifier = Modifier.fillMaxSize().offset(y = 90.dp)
                ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxSize()
                ,
                text = message,
                textAlign = TextAlign.Center,
                fontSize = 60.sp,  // ✅ Corrected fontSize
                fontFamily = FontFamily.Monospace,  //  Use fontStyle separately
                color = Color.Unspecified,
                style = TextStyle(brush = Brush.linearGradient(listOf(Color.Red, Color.White, Color.Blue)))
            )

                Box(
                    modifier = Modifier.width(200.dp).height(100.dp).padding(10.dp)
                        .offset(x = 50.dp, y = 50.dp)

                        .clickable { navController.navigate("cameraPreview")
//                                val intent = Intent(context, camera_activity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                context.startActivity(intent) }
                        }
                        .zIndex(6f)
                        .shadow(19.dp, shape = RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color.White, Color.Magenta, Color.Green)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ), contentAlignment = Alignment.Center



                ) {
                    Text(
                        text = "CLICK TO GET  STARTED",
                        color = Color.Blue,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.Cursive


                    )
                }
            if(changed_ == "jj"){
                LaunchedEffect(Unit) {
                    delay(2000)
                    message = "this is the real change"
                }
            }

        }
    }


}
@Composable
@Preview
fun preview(){
    val nacCon = rememberNavController();
    Disp("WELCOME", navController = nacCon)

}
