package com.example.themealapp.api

import com.example.themealapp.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

//www.themealdb.com/api/json/v1/1/search.php?f=a
interface MealApi {
    @GET("search.php")
    suspend fun searchMeals(@Query("f") letter : String = "a"): MealResponse

}