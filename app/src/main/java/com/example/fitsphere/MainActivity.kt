package com.example.fitsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fitsphere.ui.theme.MyApplicationTheme
import com.example.fitsphere.MainScreen
//import org.maplibre.android.Mapbox

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 MapLibre（模拟 Mapbox 初始化方式）
        //Mapbox.getInstance(
        //applicationContext,
        //    "pk.eyJ1IjoieHkxMTE5IiwiYSI6ImNtOWpuZHZwcDBmYmkybHB4ajVqbWVmZXEifQ.gtnPJLTokTT7d2xrYzTgYA" // ← 这里使用你的 MapTiler token 或空字符串（如不需要 token）
       // )

        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}
