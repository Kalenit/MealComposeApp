package com.example.themealapp.navigation

// Navigation.kt - COMPLETE IMPLEMENTATION
sealed class Screen(val route: String) {
    object Meals : Screen("meals")
    object MealDetail : Screen("meal_detail/{mealId}") {
        fun createRoute(mealId: String) = "meal_detail/$mealId"  // ✅ THIS WAS MISSING!
    }
}

