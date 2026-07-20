package com.bignerdranch.android.groceryplanner

import GroceryViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(
    viewModel: GroceryViewModel
){
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            //Display days of the week as a header
            TopAppBar(
                title = {MealPlanHeader()},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Background Color
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            //Display floating action button
            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_item),
                    contentDescription = "Add Item"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryFixedDim
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            //Display calendar buttons
            CalendarButtons(viewModel)

            //Display Meal Plans
            DropdownCard(
                headerText = stringResource(R.string.mealsCardHeaderText),
                content = {
                    Text(stringResource(R.string.placeholderText))
                }
            )

            //Display Shopping List
            DropdownCard(
                headerText = stringResource(R.string.listCardHeaderText),
                content = {
                    Text(stringResource(R.string.placeholderText))
                }
            )
        }
    }
}

@Composable
fun MealPlanHeader() {
    Column {
        //Displays the header text
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.mealScreenTopBarText),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun CalendarButtons(
    viewModel: GroceryViewModel
) {
    Column {
        //Used to remember which of the 7 days is selected
        val selectedDay = viewModel.selectedDayOfWeek

        Card (
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RectangleShape
        ) {
            //Spacer
            Spacer(modifier = Modifier.height(8.dp))

            //Displays 7 days of the week as buttons in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(7) { index ->
                    val isSelected = index == selectedDay
                    val firstDayOfWeekMonthString = viewModel.firstDayOfWeek.monthValue.toString()
                    val firstDayOfWeekDayString = (viewModel.firstDayOfWeek.dayOfMonth + index).toString()

                    val dayOfWeek = listOf(
                        stringResource(R.string.calendarButtonMonday),
                        stringResource(R.string.calendarButtonTuesday),
                        stringResource(R.string.calendarButtonWednesday),
                        stringResource(R.string.calendarButtonThursday),
                        stringResource(R.string.calendarButtonFriday),
                        stringResource(R.string.calendarButtonSaturday),
                        stringResource(R.string.calendarButtonSunday)
                    )[index]

                    val outlineColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

                    OutlinedCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable {
                                viewModel.setSelectedDay(index)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(width = 2.dp, color = outlineColor)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dayOfWeek\n" +
                                        "$firstDayOfWeekMonthString/$firstDayOfWeekDayString",
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                color =
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                }
            }

            //Spacer
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun DropdownCard(
    headerText: String,
    content: @Composable () -> Unit,
){
    Spacer(modifier = Modifier.height(4.dp))
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth(1f)
    ){
        var expanded by remember { mutableStateOf(true) }

        //Displays a dropdown header that hides or displays content when tapped
        Column(
            Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            //Display header text with dropdown icon
            Row {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                if(!expanded){
                    Icon(
                        painter = painterResource(R.drawable.drop_down),
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(32.dp)
                    )
                }else{
                    Icon(
                        painter = painterResource(R.drawable.drop_down),
                        contentDescription = "Dropdown",
                        modifier = Modifier
                            .size(32.dp)
                            .scale(scaleX = 1f, scaleY = -1f)
                    )
                }
            }

            //Display content when expanded
            AnimatedVisibility(expanded) {
                content()
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun MealScreenPreview() {
    val viewModel = GroceryViewModel()

    MealScreen(viewModel)
}