package com.example.bia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bia.data.MealEntry
import com.example.bia.data.database.MealDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class NutritionViewModel(private val mealDao: MealDao) : ViewModel() {
    private fun calculateTodayRange(): Pair<Instant, Instant> {
        val zone = ZoneId.systemDefault() //timezone
        val today = LocalDate.now()

        val start = today.atStartOfDay(zone).toInstant()
        val end = today.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant()

        return Pair(start, end)
    }

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
}
