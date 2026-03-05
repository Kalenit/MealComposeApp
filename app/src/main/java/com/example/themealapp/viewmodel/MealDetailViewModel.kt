package com.example.themealapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themealapp.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MealDetailViewModel @Inject constructor(private val repository: MealRepository) : ViewModel() {

    private val _detailState = MutableLiveData<MealDetailState>(MealDetailState.Loading)
    val detailState: LiveData<MealDetailState> = _detailState

    fun fetchMealDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = MealDetailState.Loading
            val result = repository.getMealDetail(id)
            _detailState.value = if (result.isSuccess) {
                MealDetailState.Success(result.getOrNull()!!)
            } else {
                MealDetailState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
