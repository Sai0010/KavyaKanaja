package com.example.kavyakanaja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.NavGraph
import com.example.kavyakanaja.ui.theme.KavyaKanajaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize poem data from JSON
        SampleData.init(this)
        enableEdgeToEdge()
        setContent {
            KavyaKanajaTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}