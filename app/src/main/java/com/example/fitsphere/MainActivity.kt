package com.example.myapplication
import com.example.myapplication.di.DatabaseProvider
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.ui.theme.MyApplicationTheme
//import com.example.myapplication.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProvider.initDatabase(applicationContext)
        setContent {
            MyApplicationTheme {
                // Set the content to DietScreen or Navigation host
                DietScreen()  // 如果你只想先调试 Diet 页面
                // AppNavigation() // 如果你已经设置完整导航结构了
            }
        }
    }
}
