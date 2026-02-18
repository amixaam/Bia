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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bia.data.FoodItem
import com.example.bia.data.MeasureUnit
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodScreen(
    viewModel: NutritionViewModel,
    onBackClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var caloriesText by rememberSaveable { mutableStateOf("") }
    var proteinText by rememberSaveable { mutableStateOf("") }
    var carbsText by rememberSaveable { mutableStateOf("") }
    var fatText by rememberSaveable { mutableStateOf("") }
    var unit by rememberSaveable { mutableStateOf(MeasureUnit.G) }

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
                title = { Text("Create Food Item") },
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

                    viewModel.addFood(
                        FoodItem(
                            name = name.trim(),
                            brand = brand.trim().ifBlank { null },
                            calories = calories!!,
                            protein = protein!!,
                            carbs = carbs!!,
                            fat = fat!!,
                            unit = unit,
                            lastUsed = Instant.now()
                        )
                    )
                    onBackClick()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Food Item")
            }
        }
    }
}
