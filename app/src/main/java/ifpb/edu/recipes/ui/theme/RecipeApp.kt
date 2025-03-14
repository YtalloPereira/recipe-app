package ifpb.edu.recipes.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ifpb.edu.recipes.data.model.Recipe
import ifpb.edu.recipes.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApp() {
    val recipes = remember { mutableStateListOf<Recipe>() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getRecipes()
                recipes.addAll(response.recipes ?: emptyList())
            } catch (e: Exception) {
                Log.e("RecipeApp", "Erro ao carregar receitas", e)
                snackbarHostState.showSnackbar("Erro ao carregar receitas")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Delicious Recipes") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(recipes) { recipe ->
                    RecipeItem(recipe) { recipeId ->
                        coroutineScope.launch {
                            try {
                                // Carregar ingredientes
                                val ingredientResponse = try {
                                    RetrofitInstance.api.getIngredients(recipeId)
                                } catch (e: Exception) {
                                    Log.e("RecipeApp", "Erro ao carregar ingredientes", e)
                                    null
                                }

                                // Processar ingredientes e adicionar URLs completas
                                val processedIngredients = ingredientResponse?.ingredients?.map { ingredient ->
                                    val imageUrl = if (ingredient.image?.startsWith("http") == true) {
                                        ingredient.image
                                    } else {
                                        "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
                                    }
                                    ingredient.copy(image = imageUrl)
                                }

                                // Carregar instruções
                                val instructions = try {
                                    RetrofitInstance.api.getInstructions(recipeId)
                                } catch (e: Exception) {
                                    Log.e("RecipeApp", "Erro ao carregar instruções", e)
                                    null
                                }

                                val index = recipes.indexOfFirst { it.id == recipeId }
                                if (index != -1) {
                                    val updatedRecipe = recipes[index].copy(
                                        ingredients = processedIngredients,
                                        analyzedInstructions = instructions
                                    )
                                    recipes[index] = updatedRecipe
                                }
                            } catch (e: Exception) {
                                Log.e("RecipeApp", "Erro ao atualizar receita", e)
                                snackbarHostState.showSnackbar("Erro ao carregar detalhes da receita")
                            }
                        }
                    }
                }
            }
        }
    }
}