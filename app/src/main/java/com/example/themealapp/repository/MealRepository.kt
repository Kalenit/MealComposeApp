package com.example.themealapp.repository

import com.example.themealapp.api.MealApi
import com.example.themealapp.model.Meal
import com.example.themealapp.model.MealResponse
import com.example.themealapp.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit


// manual constructor injection
// TODO: Hilt DI
class MealRepository (private val api: MealApi = RetrofitClient.api) {
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