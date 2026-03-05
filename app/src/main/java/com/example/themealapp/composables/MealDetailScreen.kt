package com.example.themealapp.composables

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.themealapp.model.MealDetail
import com.example.themealapp.repository.MealRepository
import com.example.themealapp.viewmodel.MealDetailState
import com.example.themealapp.viewmodel.MealDetailViewModel
import com.example.themealapp.viewmodel.MealViewModel
import kotlin.getValue

@Composable
fun MealDetailScreen(
    detailViewModel: MealDetailViewModel= hiltViewModel(),
    mealId: String,
    navController: NavController,
) {


    // Fetch details when screen loads
    LaunchedEffect(mealId) {
        detailViewModel.fetchMealDetail(mealId)
    }

    val state by detailViewModel.detailState.observeAsState(MealDetailState.Loading)

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is MealDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MealDetailState.Success -> {
                MealDetailContent(meal = (state as MealDetailState.Success).meal, navController = navController)
            }
            is MealDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error: ${(state as MealDetailState.Error).message}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { detailViewModel.fetchMealDetail(mealId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealDetailContent(
    meal: MealDetail,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = meal.strInstructions ?: "No instructions available",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleLarge
            )
           //  IngredientsList(meal)
        }

        item {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Meals")
            }
        }
    }
}

@Composable
fun IngredientsList(meal: MealDetail) {
    val ingredients = listOfNotNull(
        meal.strIngredient1 to meal.strMeasure1,
        meal.strIngredient2 to meal.strMeasure2,
        meal.strIngredient3 to meal.strMeasure3,
        meal.strIngredient4 to meal.strMeasure4,
        meal.strIngredient5 to meal.strMeasure5
    ).filter { (ingredient, _) -> !ingredient.isNullOrEmpty() }

    LazyColumn {
        items(ingredients) { (ingredient, measure) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "$ingredient: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = measure ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}