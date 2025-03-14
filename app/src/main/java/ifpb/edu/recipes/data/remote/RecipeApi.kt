package ifpb.edu.recipes.data.remote

import ifpb.edu.recipes.data.model.IngredientResponse
import ifpb.edu.recipes.data.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipes/random?number=10&apiKey=4f285b25f9df4505a2e5b63dce3d8fab")
    suspend fun getRecipes(): RecipeResponse

    @GET("recipes/{id}/ingredientWidget.json?apiKey=4f285b25f9df4505a2e5b63dce3d8fab")
    suspend fun getIngredients(@Path("id") recipeId: Int): IngredientResponse
}