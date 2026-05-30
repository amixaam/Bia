package com.example.bia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bia.data.dataclass.Food
import com.example.bia.data.MeasureUnit
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodScreen(
    viewModel: NutritionViewModel,
    foodId: Int = -1,
    onBackClick: () -> Unit,
) {
    val allFoods by viewModel.allFoods.collectAsState()

    val editFood = remember(foodId, allFoods) {
        if (foodId != -1) allFoods.find { it.id == foodId } else null
    }

    var name by rememberSaveable(editFood) { mutableStateOf(editFood?.name ?: "") }
    var brand by rememberSaveable(editFood) { mutableStateOf(editFood?.brand ?: "") }
    var caloriesText by rememberSaveable(editFood) { mutableStateOf(editFood?.calories?.toString() ?: "") }
    var proteinText by rememberSaveable(editFood) { mutableStateOf(editFood?.protein?.toString() ?: "") }
    var carbsText by rememberSaveable(editFood) { mutableStateOf(editFood?.carbs?.toString() ?: "") }
    var fatText by rememberSaveable(editFood) { mutableStateOf(editFood?.fat?.toString() ?: "") }
    var unit by rememberSaveable(editFood) { mutableStateOf(editFood?.unit ?: MeasureUnit.G) }

    val calories = caloriesText.toIntOrNull()
    val protein = proteinText.toFloatOrNull()
    val carbs = carbsText.toFloatOrNull()
    val fat = fatText.toFloatOrNull()

    val isValid = name.isNotBlank() &&
            calories != null && calories >= 0 &&
            protein != null && protein >= 0f &&
            carbs != null && carbs >= 0f &&
            fat != null && fat >= 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editFood != null) "Edit food" else "Register food") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Brand (optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Unit basis")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = unit == MeasureUnit.G,
                    onClick = { unit = MeasureUnit.G },
                    label = { Text("100 g") }
                )
                FilterChip(
                    selected = unit == MeasureUnit.ML,
                    onClick = { unit = MeasureUnit.ML },
                    label = { Text("100 ml") }
                )
            }

            OutlinedTextField(
                value = caloriesText,
                onValueChange = { caloriesText = it },
                label = { Text("Calories") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = proteinText,
                onValueChange = { proteinText = it },
                label = { Text("Protein (g)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = carbsText,
                onValueChange = { carbsText = it },
                label = { Text("Carbs (g)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fatText,
                onValueChange = { fatText = it },
                label = { Text("Fat (g)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (!isValid) return@Button

                    val food = Food(
                        id = editFood?.id ?: 0,
                        name = name.trim(),
                        brand = brand.trim().ifBlank { null },
                        calories = calories,
                        protein = protein,
                        carbs = carbs,
                        fat = fat,
                        unit = unit,
                        lastUsed = Instant.now()
                    )

                    if (editFood != null) {
                        viewModel.updateFood(food)
                    } else {
                        viewModel.addFood(food)
                    }

                    onBackClick()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
