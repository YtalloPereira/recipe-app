package ifpb.edu.recipes.data.model

data class Instruction(
    val number: Int,
    val step: String,
    val ingredients: List<InstructionIngredient>? = null
)