package com.example.bia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bia.data.MealEntry
import com.example.bia.data.MealGroup
import com.example.bia.data.database.MealDao
import com.example.bia.data.mealCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NutritionViewModel(private val mealDao: MealDao) : ViewModel() {
    private fun calculateTodayRange(): Pair<Instant, Instant> {
        val zone = ZoneId.systemDefault() //timezone
        val today = LocalDate.now()

        val start = today.atStartOfDay(zone).toInstant()
        val end = today.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant()

        return Pair(start, end)
    }

//    // group eaten foods. any food within 30 minutes gets grouped.
//    fun groupMeals(entries: List<MealEntry>): List<MealGroup> {
//        if (entries.isEmpty()) return emptyList()
//
//        val sortedEntries = entries.sortedBy { it.timestamp }
//
//        val groups = sortedEntries.fold(mutableListOf<MutableList<MealEntry>>()) { groups, entry ->
//            val lastGroup = groups.lastOrNull()
//            if (lastGroup == null || Duration.between(lastGroup.last().timestamp, entry.timestamp).toMinutes() >= 30) {
//                groups.add(mutableListOf(entry))
//            } else {
//                lastGroup.add(entry)
//            }
//            groups
//        }
//
//        val finalizedGroups = mutableListOf<MealGroup>()
//
//        // groups without duplicates
//        for (group in groups) {
//            val totalCalories = group.sumOf { it.caloriesSnapshot }
//            val lastSavedTime = group.last().timestamp.atZone(ZoneId.systemDefault())
//
//            finalizedGroups.add(
//                MealGroup(
//                    group,
//                    totalCalories,
//                    lastSavedTime.toInstant()
//                )
//            )
//        }
//
//        return finalizedGroups
//    }

    val todayRange = calculateTodayRange()
    val mealEntries: StateFlow<List<MealEntry>> = mealDao
        .getMealsFromDateRange(todayRange.first, todayRange.second)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    //TODO: Later switch this out with an actual estimate
    val _calorieGoal = MutableStateFlow<Int>(2500)
    val calorieGoal = _calorieGoal.asStateFlow()

    val totalCaloriesConsumed: StateFlow<Int> = mealEntries
        .map { list ->
            list.sumOf { it.caloriesSnapshot }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )


    fun addMeal(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealDao.saveMeal(mealEntry)
        }
    }

    fun deleteAllMeals() {
        viewModelScope.launch {
            mealDao.deleteAllMealsFromDateRange(todayRange.first, todayRange.second)
        }
    }
}
