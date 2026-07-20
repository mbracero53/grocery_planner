package com.bignerdranch.android.groceryplanner

import GroceryViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//Set up Navigation Bar Destinations
enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String
) {
    MealPlan("MealScreen", "Meal Plan", R.drawable.meal_plan, "Meal Plan"),
    Recipes("RecipeScreen", "Recipes", R.drawable.recipe_book, "Recipes"),
    //Calendar("Calendar", "Calendar", R.drawable.calendar, "Calendar")
}

//Initialize Main Activity
class MainActivity : ComponentActivity() {
    private val groceryViewModel: GroceryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(groceryViewModel)
        }
    }
}

//Display the navigation bar and content determined by which option is selected
@Composable
fun MainScreen(
    viewModel: GroceryViewModel
){
    val navController = rememberNavController()
    val startDestination = Destination.Recipes
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = Modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = destination.icon),
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) {innerPadding ->
        NavHost(
            navController,
            startDestination = startDestination.route,
            enterTransition = {fadeIn()},
            exitTransition = {fadeOut()},
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Destination.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        Destination.MealPlan -> MealScreen(viewModel)
                        Destination.Recipes -> RecipeScreen(viewModel)
                        //Destination.Calendar -> CalendarScreen()
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun GroceryAppPreview() {
    val viewModel = GroceryViewModel()

    MainScreen(viewModel)
}