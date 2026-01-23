package com.example.bia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bia.data.FoodItem
import com.example.bia.data.MealEntry
import com.example.bia.data.MeasureUnit
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    viewModel: NutritionViewModel,
    groupId: Int, // -1 is new group
    onBackClick: () -> Unit
) {
    val allFoods by viewModel.allFoods.collectAsState(initial = emptyList())

    fun onFoodSelected(foodItem: FoodItem) {
        val newMealItem = MealEntry(
            foodId = foodItem.id,
            groupId = 0, // gonna be replaced
            quantity = 100f,
            timestamp = Instant.now(),
            caloriesSnapshot = foodItem.calories,
            nameSnapshot = foodItem.name
        )

        viewModel.addMeal(newMealItem, groupId)
        onBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Meal") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button({viewModel.addFood(
                FoodItem(
                    id = 0,
                    name = "fake food",
                    calories = 590,
                    unit = MeasureUnit.G,
                    fat = 31f,
                    carbs = 24f,
                    protein = 69f,
                    brand = null,
                    lastUsed = Instant.now()
                )
            )}) {
                Text("Create fake item")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(allFoods) { food ->
                    ListItem(
                        headlineContent = { Text(food.name) },
                        supportingContent = { Text("${food.calories} kcal per 100g") },
                        modifier = Modifier.clickable { onFoodSelected(food) }
                    )
                }
            }
        }
    }
}