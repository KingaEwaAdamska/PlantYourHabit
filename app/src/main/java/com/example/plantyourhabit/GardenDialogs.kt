package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green900

@Composable
internal fun AddHabitDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a habit") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
internal fun PlantPickerDialog(
    habit: Habit,
    availablePlants: List<String>,
    selectedPlant: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose a plant") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(GardenTokens.SmallInset)) {
                Text(
                    "Pick a flower for ${habit.name}.",
                    color = Green700,
                    fontSize = GardenTokens.CardTitleText
                )
                availablePlants.forEach { plant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(GardenTokens.CardInset))
                            .background(if (plant == selectedPlant) Green200 else CreamSurface)
                            .clickable { onSelect(plant) }
                            .padding(horizontal = GardenTokens.ContentInset, vertical = GardenTokens.LabelInset),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlantArtwork(
                            plant,
                            habit = habit.copy(careDays = exampleCareDays(MAX_STREAK_DAY)),
                            today = LocalDate.now(),
                            modifier = Modifier.size(GardenTokens.PlantPreviewWidth, GardenTokens.PlantPickerPreviewHeight)
                        )
                        Text(
                            plant.displayName(),
                            modifier = Modifier.weight(1f).padding(start = GardenTokens.ContentInset),
                            color = Green900,
                            fontWeight = FontWeight.Medium
                        )
                        if (plant == selectedPlant) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "Selected",
                                tint = Green700,
                                modifier = Modifier.size(GardenTokens.CardTitleText.value.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

private fun String.displayName(): String = when (this) {
    "monstera" -> "Monstera"
    "fern" -> "Fern"
    "succulent" -> "Succulent"
    "chinese_money_plant" -> "Chinese money plant"
    "cactus" -> "Desert cactus"
    "orchid" -> "Orchid"
    else -> replaceFirstChar { it.uppercase() }
}
