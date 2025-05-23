package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.mainScreenPage.AppNav
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.WellKnownTileServer



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 Mapbox（如果你用地图）
        Mapbox.getInstance(
            this,
            "pk.eyJ1IjoieHkxMTE5IiwiYSI6ImNtOWpuZHZwcDBmYmkybHB4ajVqbWVmZXEifQ.gtnPJLTokTT7d2xrYzTgYA",
            WellKnownTileServer.Mapbox
        )

        setContent {
            MyApplicationTheme {
                AppNav() // ✅ 加载你的主界面导航体系
            }
        }
    }
}
