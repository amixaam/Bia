package com.example.bia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    onBackClick: () -> Unit,
    onCreateFoodClick: () -> Unit,
    onScanBarcodeClick: () -> Unit
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCreateFoodClick,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Create Food Item")
                }
                Button(
                    onClick = onScanBarcodeClick,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Scan barcode")
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(allFoods) { food ->
                    ListItem(
                        headlineContent = { Text(food.name) },
                        supportingContent = {
                            val unitText = if (food.unit == MeasureUnit.G) "100g" else "100ml"
                            Text("${food.calories} kcal per $unitText")
                        },
                        modifier = Modifier.clickable { onFoodSelected(food) }
                    )
                }
            }
        }
    }
}
