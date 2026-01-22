package com.example.bia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bia.data.MealEntry
import com.example.bia.ui.composables.CalorieRing
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.Instant

@Composable
fun HomeScreen(viewModel: NutritionViewModel) {
    val meals by viewModel.mealEntries.collectAsState()
    val consumed by viewModel.totalCaloriesConsumed.collectAsState()
    val goal by viewModel.calorieGoal.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalorieRing(
                consumed = consumed,
                goal = goal
            )

            Spacer(modifier = Modifier.height(24.dp))

            // add food
            Button(onClick = {
                viewModel.addMeal(
                    MealEntry(
                        foodId = 0,
                        quantity = 1f,
                        timestamp = Instant.now(),
                        caloriesSnapshot = 200,
                        nameSnapshot = "Quick Snack"
                    )
                )
            }) {
                Text("Add 200 kcal Snack")
            }

            // remove all food
            Button(onClick = {
                viewModel.deleteAllMeals()
            }) {
                Text("Remove All snacks")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Today's History",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(meals) { meal ->
                    Text("Eaten: ${meal.nameSnapshot} (${meal.caloriesSnapshot} kcal)")
                }
            }
        }
    }
}