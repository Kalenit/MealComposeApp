package com.example.themealapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themealapp.repository.MealRepository
import kotlinx.coroutines.launch


// manual constructor injection
// TODO: Hilt DI

class MealViewModel (private val mealRepository: MealRepository): ViewModel(){

    private val _mealState = MutableLiveData<MealState>(MealState.Loading)
    val mealState: LiveData<MealState> = _mealState


    // 2 ways
    // LaunchedEffect
    // init

    init {
        fetchMeals()
    }


    private fun fetchMeals(){
        viewModelScope.launch {
            _mealState.value = MealState.Loading
            val result = mealRepository.getMeals()
            _mealState.value = if(result.isSuccess){
                val meals = result.getOrNull() ?: emptyList()
                MealState.Success(meals)
            } else {
                MealState.Error(result.exceptionOrNull() ?.message ?: "Unknown Error")
            }
        }
    }

}