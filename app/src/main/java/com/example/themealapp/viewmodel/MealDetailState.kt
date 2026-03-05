package com.example.themealapp.viewmodel

import com.example.themealapp.model.MealDetail

sealed class MealDetailState {
    object Loading : MealDetailState()
    data class Success(val meal: MealDetail) : MealDetailState()
    data class Error(val message: String) : MealDetailState()
}
