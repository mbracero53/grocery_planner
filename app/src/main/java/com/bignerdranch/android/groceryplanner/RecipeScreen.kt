package com.bignerdranch.android.groceryplanner

import GroceryViewModel
import Ingredient
import Recipe
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: GroceryViewModel
){
    var showDialog by remember { mutableStateOf(false) }

    if(!showDialog){
        //Display list of recipes if add new items dialog is not toggled on
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { RecipeHeader() },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Background Color
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                //Display floating action button
                FloatingActionButton(
                    onClick = {showDialog = true},
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_item),
                        contentDescription = "Add Item"
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            val list = viewModel.recipeList
            // lazy column for displaying list of recipes
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                // populating items for list
                itemsIndexed(list) { index, item ->
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(15.dp)
                    )

                    HorizontalDivider()
                }
            }
        }
    }else{
        //Display the menu to add new items to recipe or edit recipe
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { AddNewRecipeHeader() },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Background Color
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.clickable{
                        showDialog = !showDialog
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                AddNewRecipeMenu(
                    viewModel,
                    onAdd = {showDialog = !showDialog}
                )
            }
        }
    }
}

@Composable
fun RecipeHeader() {
    Column {
        //Displays the header text
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.recipeScreenTopBarText),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun AddNewRecipeHeader() {
    Column {
        //Displays the header text
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Icon(
                painter = painterResource(R.drawable.chevron_back),
                contentDescription = "Back",
                Modifier.size(32.dp)
            )
            Text(
                text = stringResource(R.string.addRecipeTopBarBackText),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AddNewRecipeMenu(
    viewModel: GroceryViewModel,
    onAdd: () -> Unit
){
    //Store the values for the new recipe
    var recipeName by remember { mutableStateOf("") }
    val ingredients = remember {
        mutableStateListOf<Ingredient>()
    }

    //Store the values for new ingredients
    var ingredientName by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }

    //Disable add new recipe button if either field is blank
    val canAddNewRecipe by remember {
        derivedStateOf { recipeName.isNotBlank() && ingredients.isNotEmpty()}
    }

    //Disable add new ingredient button if either field is blank
    val canAddNewIngredient by remember {
        derivedStateOf { ingredientName.isNotBlank() && storeName.isNotBlank()}
    }

    Column (
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        //Display the textfield for entering the recipe name
        Text(
            text = stringResource(R.string.addNewRecipeName),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        HorizontalDivider()
        TextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text(stringResource(R.string.addNewRecipeNameFieldLabel)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        //Display a list of ingredients for the recipe
        Text(
            text = stringResource(R.string.addNewIngredients),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        HorizontalDivider()
        LazyColumn {
            items(ingredients) { item ->
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp,vertical = 4.dp)
                )
                Text(
                    text = item.storeName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp,vertical = 0.dp)
                )
                HorizontalDivider()
            }
        }

        //Display button to add new ingredient to recipe
        Row{
            TextField(
                value = ingredientName,
                onValueChange = { ingredientName = it },
                label = { Text(stringResource(R.string.ingredientNameFieldLabel)) },
                singleLine = true,
                modifier = Modifier
                    .height(56.dp)
                    .weight(0.5f)
            )
            TextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text(stringResource(R.string.storeNameFieldLabel)) },
                singleLine = true,
                modifier = Modifier
                    .height(56.dp)
                    .weight(0.5f)
            )
        }
        Button(
            onClick = {
                ingredients.add(
                    Ingredient(ingredientName, storeName)
                )
            },
            enabled = canAddNewIngredient
        ) {
            Text(
                text = "Add Ingredient",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        //Display button to add recipe to recipe list
        Button(
            onClick = {
                viewModel.addRecipetoList(
                    Recipe(
                        name = recipeName,
                        ingredients = ingredients
                    )
                )
                onAdd()
            },
            enabled = canAddNewRecipe,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Add New Recipe",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun RecipeScreenPreview() {
    val viewModel = GroceryViewModel()

    RecipeScreen(viewModel)
}