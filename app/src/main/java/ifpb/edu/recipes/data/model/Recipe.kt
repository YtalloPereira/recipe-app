package ifpb.edu.recipes.data.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val ingredients: List<Ingredient>? = null,
    val analyzedInstructions: List<AnalyzedInstruction>? = null
)