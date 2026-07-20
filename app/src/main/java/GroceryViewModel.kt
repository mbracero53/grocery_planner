import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class Ingredient(
    val name: String,
    val storeName: String
)

data class Recipe(
    val name: String,
    val ingredients: List<Ingredient>
)

class GroceryViewModel : ViewModel() {
    //Initialize list of recipes
    /*
    TODO Need to figure out how to properly support loading recipes from/into a JSON file, it looks like the most
    common import for this is broken so I'll need to do more research to properly apply this to new versions of the app. If
    I can't save the recipes or meal plans it's little more than a proof of concept.
     */
    val recipeList = mutableListOf(
        Recipe(
            name = "Hamburgers",
            ingredients = listOf(
                Ingredient("Hamburger Buns","Walmart"),
                Ingredient("Ground Beef","Walmart"),
                Ingredient("Lettuce","Publix"),
                Ingredient("Tomatoes","Publix")
            )
        ),
        Recipe(
            name = "Tacos",
            ingredients = listOf(
                Ingredient("Taco Shells","Walmart"),
                Ingredient("Ground Beef","Walmart"),
                Ingredient("Shredded Mild Cheddar","Walmart"),
                Ingredient("Lettuce","Publix"),
                Ingredient("Tomatoes","Publix")
            )
        )
    )

    //Get the current day of the week and the first
    val currentDate = LocalDate.now()!!
    val firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))!!

    //Used for the current day of the week selected in MealScreen's CalendarHeader composable
    private val _selectedDayOfWeek: MutableIntState = mutableIntStateOf(currentDate.dayOfWeek.value - 1)

    val selectedDayOfWeek: Int
        get() = _selectedDayOfWeek.intValue

    //Functions
    fun setSelectedDay(day: Int) {
        _selectedDayOfWeek.intValue = day
    }

    fun addRecipetoList(newRecipe: Recipe){
        recipeList.add(newRecipe)
    }
}