package com.example.bia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bia.data.MeasureUnit
import com.example.bia.data.OpenFoodFactsApi
import com.example.bia.data.dataclass.Food
import com.example.bia.data.dataclass.Serving
import com.example.bia.data.dataclass.Meal
import com.example.bia.data.dataclass.ScannedProduct
import com.example.bia.data.database.FoodDao
import com.example.bia.data.database.MealDao
import com.example.bia.data.database.ServingDao
import com.example.bia.util.generateMealTitle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class NutritionViewModel(
    private val servingDao: ServingDao,
    private val foodDao: FoodDao,
    private val mealDao: MealDao,
    private val foodApi: OpenFoodFactsApi
) : ViewModel() {
    private fun calculateTodayRange(): Pair<Instant, Instant> {
        val zone = ZoneId.systemDefault() //timezone
        val today = LocalDate.now()

        val start = today.atStartOfDay(zone).toInstant()
        val end = today.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant()

        return Pair(start, end)
    }

    val _todayRange = MutableStateFlow(calculateTodayRange())
    val todayRange: StateFlow<Pair<Instant, Instant>> = _todayRange

    fun refreshDate() {
        _todayRange.value = calculateTodayRange()
    }

    val todaysMeals: StateFlow<List<Meal>> = _todayRange
        .flatMapLatest { range ->
            mealDao.getMealsFromDateRange(range.first, range.second)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todaysServings: StateFlow<List<Serving>> = _todayRange
        .flatMapLatest { range ->
            servingDao.getServingsFromDateRange(range.first, range.second)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allFoods: StateFlow<List<Food>> = foodDao
        .getAllFood()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //TODO: Later switch this out with an actual estimate
    val _calorieGoal = MutableStateFlow<Int>(2500)
    val calorieGoal = _calorieGoal.asStateFlow()

    val totalCaloriesConsumed: StateFlow<Int> = todaysServings
        .map { entries -> entries.sumOf { (it.caloriesSnapshot * (it.quantity/100f)).toInt() }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun updateFoodLastUsed(food: Food) {
        viewModelScope.launch {
            foodDao.updateFood(food.copy(lastUsed = Instant.now()))
        }
    }

    fun addServing(serving: Serving, mealId: Int) {
        viewModelScope.launch {
            val finalMealId = if (mealId == -1) {
                mealDao.createMeal(
                    Meal(
                        title = generateMealTitle(Instant.now()),
                        pinned = false,
                        timestamp = Instant.now()
                    )
                ).toInt()
            } else {
                mealId
            }

            val newServing = serving.copy(mealId = finalMealId)
            servingDao.createServing(newServing)
        }
    }

    suspend fun getFoodById(id: Int): Food? = foodDao.getFoodById(id)

    fun addFood(food: Food) {
        viewModelScope.launch {
            foodDao.createFood(food)
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            foodDao.updateFood(food)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            servingDao.deleteAllServings()
            mealDao.deleteAllMeals()
            foodDao.deleteAllFood()
        }
    }

    fun clearAllFoods() {
        viewModelScope.launch {
            foodDao.deleteAllFood()
        }
    }

    fun clearAllServings() {
        viewModelScope.launch {
            servingDao.deleteAllServings()
        }
    }

    fun clearAllMeals() {
        viewModelScope.launch {
            mealDao.deleteAllMeals()
        }
    }

    fun scanBarcode(barcode: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {

                if (false) {
                    onError("test")
                    return@launch
                }

                // if food already cached
                val cachedItem = foodDao.getFoodByBarcode(barcode)
                if (cachedItem != null) {
                    onSuccess()
                    return@launch
                }

                // fetch from api
                val response = foodApi.getProduct(barcode)
                if (response.status != 1 || response.product == null) {
                    onError("Product not found in database")
                    return@launch
                }

                // make food item
                val nutriments = response.product.nutriments
                val newItemId = foodDao.createFood(
                    Food(
                        name = response.product.name?.takeIf { it.isNotBlank() } ?: "Unknown Product",
                        brand = response.product.brands?.takeIf { it.isNotBlank() } ?: "Unknown Brand",
                        calories = (nutriments?.calories100g ?: 0.0).toInt().coerceAtLeast(0),
                        protein = (nutriments?.protein100g ?: 0.0).toFloat().coerceAtLeast(0f),
                        carbs = (nutriments?.carbs100g ?: 0.0).toFloat().coerceAtLeast(0f),
                        fat = (nutriments?.fat100g ?: 0.0).toFloat().coerceAtLeast(0f),
                        unit = MeasureUnit.G,
                        lastUsed = Instant.now()
                    )
                ).toInt()

                foodDao.insertScannedProduct(
                    ScannedProduct(barcode = barcode, foodId = newItemId)
                )

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Something went wrong")
            }
        }
    }

}
