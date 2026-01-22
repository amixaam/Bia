package com.example.bia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bia.data.database.AppDatabase
import com.example.bia.ui.screens.HomeScreen
import com.example.bia.ui.theme.BiaTheme
import com.example.bia.ui.viewmodel.NutritionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val mealDao = db.mealDao()

        val viewModel = NutritionViewModel(mealDao)

        enableEdgeToEdge()
        setContent {
            BiaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(viewModel)
                }
            }
        }
    }
}
