package ifpb.edu.recipes.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ifpb.edu.recipes.data.model.Recipe
import ifpb.edu.recipes.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApp() {
    val recipes = remember { mutableStateListOf<Recipe>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = RetrofitInstance.api.getRecipes()
            recipes.addAll(response.recipes)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Delicious Recipes") })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(recipes) { recipe ->
                RecipeItem(recipe) { recipeId ->
                    coroutineScope.launch {
                        try {
                            val ingredientResponse = RetrofitInstance.api.getIngredients(recipeId)
                            val ingredientNames = ingredientResponse.ingredients.map { it.name }

                            val index = recipes.indexOfFirst { it.id == recipeId }
                            if (index != -1) {
                                val updatedRecipe = recipes[index].copy(ingredients = ingredientNames)
                                recipes[index] = updatedRecipe
                            }
                        } catch (e: Exception) {
                            Log.e("RecipeApp", "Erro ao carregar ingredientes", e)
                        }
                    }
                }
            }
        }
    }
}
