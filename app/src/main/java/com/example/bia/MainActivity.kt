package com.example.bia

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bia.data.database.AppDatabase
import com.example.bia.ui.screens.AddMealScreen
import com.example.bia.ui.screens.HomeScreen
import com.example.bia.ui.theme.BiaTheme
import com.example.bia.ui.viewmodel.NutritionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val mealDao = db.mealDao()
        val foodDao = db.foodDao()
        val groupDao = db.groupDao()

        val viewModel = NutritionViewModel(mealDao, foodDao, groupDao)

        enableEdgeToEdge()
        setContent {
            BiaTheme {
                val navController = rememberNavController()
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
                                { navController.popBackStack() }
                            )
                        }
                    }

                }
            }
        }
    }
}
