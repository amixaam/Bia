package com.example.bia.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bia.data.dataclass.Food
import com.example.bia.data.dataclass.Serving
import com.example.bia.data.MeasureUnit
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.Instant


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddMealScreen(
    viewModel: NutritionViewModel,
    mealId: Int, // -1 is new meal
    onBackClick: () -> Unit,
    onCreateFoodClick: () -> Unit,
    onScanBarcodeClick: () -> Unit,
    onEditFoodClick: (Int) -> Unit
) {
    val allFoods by viewModel.allFoods.collectAsState(initial = emptyList())
    var selectedFood by remember { mutableStateOf<Food?>(null) }

    // Best Practice: Defer heavy list composition by one frame to keep transition smooth
    var listVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        listVisible = true
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
        ) {
            if (listVisible) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(allFoods) { food ->
                        ListItem(
                            headlineContent = { Text(food.name) },
                            supportingContent = {
                                val unitText = if (food.unit == MeasureUnit.G) "100g" else "100ml"
                                Text("${food.calories} kcal per $unitText")
                            },
                            trailingContent = {
                                IconButton(onClick = { onEditFoodClick(food.id) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Food")
                                }
                            },
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = { selectedFood = food },
                                    onLongClick = { onEditFoodClick(food.id) }
                                )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {Text("Search food...")},
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onScanBarcodeClick,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan barcode", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                IconButton(
                    onClick = onCreateFoodClick,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Register Food", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
        if (selectedFood != null) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { selectedFood = null},
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    var quantityText by rememberSaveable { mutableStateOf("") }
                    val quantity = remember(quantityText) { quantityText.toFloatOrNull() ?: 0f }

                    val food = selectedFood ?: return@Column
                    val scale = quantity / 100f

                    val displayCals = if (quantityText.isEmpty()) food.calories else (food.calories * scale).toInt()
                    val displayProtein = if (quantityText.isEmpty()) food.protein else (food.protein * scale)
                    val displayCarbs = if (quantityText.isEmpty()) food.carbs else (food.carbs * scale)
                    val displayFat = if (quantityText.isEmpty()) food.fat else (food.fat * scale)

                    val focusRequester =  remember { FocusRequester() }
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "$displayCals kcal",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Protein ${"%.1f".format(displayProtein)}g")
                        Text("Carbs ${"%.1f".format(displayCarbs)}g")
                        Text("Fat ${"%.1f".format(displayFat)}g")
                    }

                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        label = { Text(if (food.unit == MeasureUnit.ML) "Milliliters" else "Grams") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val quantity = quantityText.toFloatOrNull() ?: return@KeyboardActions
                                val newServing = Serving(
                                    foodId = food.id,
                                    mealId = 0, // gonna be replaced
                                    quantity = quantity,
                                    timestamp = Instant.now(),
                                    caloriesSnapshot = food.calories,
                                    nameSnapshot = food.name
                                )

                                viewModel.updateFoodLastUsed(food)
                                viewModel.addServing(newServing, mealId)
                                onBackClick()
                            }
                        )
                    )
                }
            }
        }
    }
}
