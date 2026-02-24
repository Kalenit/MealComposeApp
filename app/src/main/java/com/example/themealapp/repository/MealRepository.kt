package com.example.themealapp.repository

import com.example.themealapp.api.MealApi
import com.example.themealapp.model.Meal
import com.example.themealapp.model.MealResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// manual constructor injection
// TODO: Hilt DI
class MealRepository (private val api: MealApi) {
    suspend fun getMeals(): Result<List<Meal>> = withContext(Dispatchers.IO){
        try {
            val response = api.searchMeals("a")
            val meals = response.meals
            Result.success(meals)
        } catch (e: Exception){
            Result.failure(e)
        }

    }
}