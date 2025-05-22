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
//                val navController = rememberNavController()
//                AppNavGraph(navController = navController)
                // Set the content to DietScreen or Navigation host
                DietScreen()
                // AppNavigation()
            }
        }
    }
}
