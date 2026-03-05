package com.example.themealapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.themealapp.ui.theme.TheMealAppTheme
import com.example.themealapp.viewmodel.MealViewModel
import retrofit2.Retrofit
import androidx.compose.runtime.getValue

// ViewModel delegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.themealapp.composables.MealDetailScreen
import com.example.themealapp.model.Meal
import com.example.themealapp.model.MealDetail
import com.example.themealapp.navigation.Screen
import com.example.themealapp.remote.RetrofitClient
import com.example.themealapp.repository.MealRepository
import com.example.themealapp.viewmodel.MealDetailState
import com.example.themealapp.viewmodel.MealDetailViewModel
import com.example.themealapp.viewmodel.MealState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MealViewModel by viewModels()
    private val detailViewModel: MealDetailViewModel by viewModels()


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMealAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//                    MealScreen(
//                        viewModel = viewModel,
//                        modifier = Modifier.padding(innerPadding),
//                        navController = TODO()
//                    )
                    AppNavigation()

                }
            }
        }
    }
}


@Composable
fun MealScreen(
    viewModel: MealViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val stateMeals by viewModel.mealState.observeAsState(MealState.Loading)

    Box(modifier = modifier.fillMaxSize()) {
        when (stateMeals) {
            is MealState.Error -> {
                // ✅ HANDLE ERROR STATE
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = "Error: ${(stateMeals as MealState.Error).message}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchMeals() }) {
                        Text("Retry")
                    }
                }
            }
            MealState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is MealState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items((stateMeals as MealState.Success).meals) { meal ->  // ✅ Fixed type casting
                        MealItem(
                            meal = meal,
                            onMealClick = { mealId ->
                                navController.navigate("meal_detail/$mealId")  // ✅ Direct route
                            }
                        )
                    }
                }
            }
            else -> {
                Text("No data")
            }
        }
    }
}


    @Composable
    fun MealItem(
        meal: Meal,
        onMealClick: (String) -> Unit
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMealClick(meal.idMeal) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = meal.strMeal
            )

        }
    }
}



    @Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheMealAppTheme {
        Greeting("Android")
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MealViewModel = viewModel()  // ✅ Create here

    NavHost(
        navController = navController,
        startDestination = "meals"
    ) {
        composable("meals") {
            MealScreen(
                viewModel = viewModel,
                navController = navController
            )
        }


        composable(
            "meal_detail/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            MealDetailScreen(
                mealId = mealId,
                navController = navController
            )
        }
    }
}


