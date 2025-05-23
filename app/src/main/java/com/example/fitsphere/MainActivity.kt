package com.example.fitsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fitsphere.ui.theme.MyApplicationTheme
import com.example.fitsphere.MainScreen
import com.example.fitsphere.di.DatabaseProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        DatabaseProvider.initDatabase(applicationContext)



        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}
