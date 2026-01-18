package com.example.bia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bia.data.MealEntry
import com.example.bia.data.database.AppDatabase
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
                    TestScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun TestScreen(viewModel: NutritionViewModel) {
    // Observe the database flow
    val meals by viewModel.mealEntries.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Button(onClick = {
            // Add a 200 kcal snack on every click
            viewModel.addMeal(
                MealEntry(
                    foodId = 0, // Placeholder
                    quantity = 1f,
                    timestamp = java.time.Instant.now(),
                    caloriesSnapshot = 200,
                    nameSnapshot = "Quick Snack"
                )
            )
        }) {
            Text("Add 200 kcal Snack")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "History:", style = MaterialTheme.typography.headlineSmall)

        // Scrollable list of meals
        LazyColumn {
            items(meals) { meal ->
                Text("Eaten: ${meal.nameSnapshot} at ${meal.timestamp}")
            }
        }
    }
}
