package com.example.bia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bia.data.OpenFoodFactsApi
import com.example.bia.data.database.AppDatabase
import com.example.bia.ui.screens.AddMealScreen
import com.example.bia.ui.screens.CreateFoodScreen
import com.example.bia.ui.screens.HomeScreen
import com.example.bia.ui.screens.ScanBarcodeScreen
import com.example.bia.ui.theme.BiaTheme
import com.example.bia.ui.viewmodel.NutritionViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Composable
private fun rememberFreshNavController(): NavHostController {
    val context = LocalContext.current
    return remember(context) {
        NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val mealDao = db.servingDao()
        val foodDao = db.foodDao()
        val groupDao = db.mealDao()
        val json = Json { ignoreUnknownKeys = true }
        val foodApi = Retrofit.Builder()
            .baseUrl(OpenFoodFactsApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(OpenFoodFactsApi::class.java)

        val viewModel = NutritionViewModel(mealDao, foodDao, groupDao, foodApi)

        enableEdgeToEdge()
        setContent {
            BiaTheme {
                val navController = rememberFreshNavController()
                Surface() {
                    NavHost(
                        navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel,
                                onAddMealClick = { groupId -> navController.navigate("AddMealScreen/$groupId")}
                            )
                        }

                        composable(
                            "AddMealScreen/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType }),
                            enterTransition = {
                                // Start from the full height of the screen (bottom)
                                slideInVertically(initialOffsetY = { it })
                            },
                            exitTransition = {
                                // Slide back down to the full height
                                slideOutVertically(targetOffsetY = { it })
                            }

                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
                            AddMealScreen(
                                viewModel,
                                groupId,
                                onBackClick = { navController.popBackStack() },
                                onCreateFoodClick = { navController.navigate("CreateFoodScreen") },
                                onScanBarcodeClick = {navController.navigate("ScanBarcodeScreen")}
                            )
                        }

                        composable("CreateFoodScreen") {
                            CreateFoodScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable("ScanBarcodeScreen") {
                            ScanBarcodeScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }

                }
            }
        }
    }
}
