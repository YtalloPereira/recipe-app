package ifpb.edu.recipes.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import ifpb.edu.recipes.data.model.Recipe

@Composable
fun RecipeItem(recipe: Recipe, onRecipeClick: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onRecipeClick(recipe.id)
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Imagem e título sempre exibidos
            Image(
                painter = rememberImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)

            if (expanded) {
                // Exibição dos ingredientes
                val ingredients = recipe.ingredients ?: emptyList()
                if (ingredients.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ingredients:", style = MaterialTheme.typography.titleSmall)
                    ingredients.forEach { ingredient ->
                        Text(text = "- $ingredient", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Exibição das instruções com verificações
                val instructions = recipe.analyzedInstructions
                if (instructions != null && instructions.isNotEmpty()) {
                    val steps = instructions[0].steps
                    if (steps != null && steps.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Instructions:", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))

                        steps.forEach { instruction ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "${instruction.number}.",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(24.dp)
                                )
                                Text(
                                    text = instruction.step,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}