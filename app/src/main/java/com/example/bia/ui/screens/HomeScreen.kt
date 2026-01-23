package com.example.bia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bia.data.MealEntry
import com.example.bia.data.MealGroup
import com.example.bia.ui.composables.CalorieRing
import com.example.bia.ui.viewmodel.NutritionViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: NutritionViewModel,
    onAddMealClick: (Int) -> Unit
) {
    val groups by viewModel.todaysGroups.collectAsState()
    val consumed by viewModel.totalCaloriesConsumed.collectAsState()
    val goal by viewModel.calorieGoal.collectAsState()
    val meals by viewModel.todaysMeals.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete all data?") },
            text = { Text("This will permanently remove all your meals and groups. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddMealClick(-1) }) {
                Icon(Icons.Default.Add, contentDescription = "Add meal")
            }
        }
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.headlineLarge,
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete all data",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(groups) { group ->
                    val groupMeals = meals.filter { it.groupId == group.id }
                    GroupList(group, groupMeals, onAddMealClick)
                }
            }
        }
    }
}

@Composable
fun GroupList(
    group: MealGroup,
    groupMeals: List<MealEntry>,
    onAddClick: (Int) -> Unit
) {
    val itemCount = groupMeals.size
    val groupCalories = groupMeals.sumOf { (it.caloriesSnapshot * (it.quantity / 100f)).toInt() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = {}),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = group.title ?: "Meal group",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = group.timestamp.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
            Text(
                text = "$itemCount items, $groupCalories calories",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        FilledTonalIconButton(
            modifier = Modifier,
            onClick = { onAddClick(group.id) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add products",
            )
        }
    }
}
