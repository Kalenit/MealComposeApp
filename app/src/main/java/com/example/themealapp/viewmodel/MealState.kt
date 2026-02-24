package com.example.themealapp.viewmodel

import com.example.themealapp.model.Meal

sealed class MealState {

    object Loading : MealState()
    data class Success(val meals: List<Meal>): MealState()
    data class Error(val message: String): MealState()

}