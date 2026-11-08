package com.example.plantyourhabit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDate
import com.example.plantyourhabit.ui.theme.BeigeLight
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.Green800
import com.example.plantyourhabit.ui.theme.LocalAppDarkTheme

@Composable
fun GardenScreen(
    habits: List<Habit>,
    activeBackground: String,
    selectedPlants: Map<Int, PlantType>,
    habitSlots: Map<Int, Int>,
    availablePlants: List<PlantType>,
    onSelectPlant: (Int, PlantType) -> Unit,
    onCreateHabit: (Habit) -> Unit,
    completedHabit: Habit?,
    onCompletionAcknowledged: () -> Unit,
    onToggleHabit: (Habit) -> Unit,
    onOpenShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingHabitId by remember { mutableStateOf<Int?>(null) }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    var today by remember { mutableStateOf(LocalDate.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(GARDEN_DATE_REFRESH_MILLIS)
            today = LocalDate.now()
        }
    }
    val progress = habits.count { today.toString() in it.careDays }
    val isDarkTheme = LocalAppDarkTheme.current
    val gardenHeaderBackground = if (isDarkTheme) Color(0xFF191B1D) else Green800
    val gardenHeaderText = if (isDarkTheme) Color(0xFFF1F2EE) else CreamBackground
    val gardenHeaderSupportingText = if (isDarkTheme) Color(0xFFC1C7C3) else Green200
    Box(modifier = modifier.fillMaxSize()) {
      Column(
          modifier = Modifier
              .fillMaxSize()
              .background(CreamBackground)
              .padding(horizontal = GardenTokens.GardenInset, vertical = GardenTokens.MicroPadding)
      ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(GardenTokens.GardenPanelCornerRadius))
                .background(CreamSurface, RoundedCornerShape(GardenTokens.GardenPanelCornerRadius))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gardenHeaderBackground)
                    .padding(horizontal = GardenTokens.SectionInset, vertical = GardenTokens.LargeInset)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (progress == habits.size) "Everything is blooming!" else "Your growing collection",
                            color = gardenHeaderText,
                            fontFamily = FontFamily.Serif,
                            fontSize = GardenTokens.GardenTitleText,
                            lineHeight = GardenTokens.LargeBodyText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Box(contentAlignment = Alignment.Center) {
                        val progressRingColor = Gold100
                        Canvas(modifier = Modifier.size(GardenTokens.GardenProgressRingSize)) {
                            drawArc(
                                color = Green300.copy(alpha = .35f),
                                startAngle = -90f,
                                sweepAngle = PROGRESS_CIRCLE_DEGREES,
                                useCenter = false,
                                style = Stroke(width = GardenTokens.TightInset.toPx())
                            )
                            drawArc(
                                color = progressRingColor,
                                startAngle = -90f,
                                sweepAngle = PROGRESS_CIRCLE_DEGREES * progress / habits.size,
                                useCenter = false,
                                style = Stroke(width = GardenTokens.TightInset.toPx())
                            )
                        }
                        Text(
                            "$progress/${habits.size}",
                            color = gardenHeaderText,
                            fontWeight = FontWeight.Bold,
                            fontSize = GardenTokens.CaptionText
                        )
                    }
                }
                Text(
                    if (progress == habits.size) "All daily care finished" else "Care for your plants daily to build a streak up to $MAX_STREAK_DAY days.",
                    modifier = Modifier.padding(top = GardenTokens.CompactInset),
                    color = gardenHeaderSupportingText,
                    fontSize = GardenTokens.SupportingText
                )
            }
            GardenRoom(
                habits = habits,
                activeBackground = activeBackground,
                selectedPlants = selectedPlants,
                habitSlots = habitSlots,
                today = today,
                onEditPlant = { editingHabitId = it },
                onToggleHabit = onToggleHabit,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            Row(
                modifier = Modifier

                    .fillMaxWidth()
                    .background(BeigeLight)
                    .padding(horizontal = GardenTokens.CardContentInset, vertical = GardenTokens.ComfortableInset),
                horizontalArrangement = Arrangement.spacedBy(GardenTokens.ComfortableInset),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (habits.size < MAX_HABITS) {
                    SoftActionButton(
                        text = "Add a habit",
                        modifier = Modifier.weight(1f),
                        onClick = { showAddHabitDialog = true },
                        icon = R.drawable.ic_add
                    )
                } else {
                    Text(
                        "You reached maximum amount of habits, perfect it before creating new ones",
                        modifier = Modifier.padding(GardenTokens.CompactInset),
                        fontSize = GardenTokens.SupportingText,
                    )
                }

            }
        }
      }
      editingHabitId?.let { habitId ->
          val habit = habits.firstOrNull { it.id == habitId }
          if (habit != null) {
              PlantPickerDialog(
                  habit = habit,
                  availablePlants = availablePlants.distinct(),
                  selectedPlant = selectedPlants[habitId] ?: habit.plant,
                  onDismiss = { editingHabitId = null },
                  onSelect = { plant ->
                      onSelectPlant(habitId, plant)
                      editingHabitId = null
                  }
              )
          }
      }
      if (showAddHabitDialog && habits.size < MAX_HABITS) {
          AddHabitDialog(
              onDismiss = { showAddHabitDialog = false },
              onCreate = { name ->
                  if (habits.size < MAX_HABITS) {
                      val nextId = (habits.maxOfOrNull(Habit::id) ?: 0) + 1
                      onCreateHabit(Habit(nextId, name, PlantType.SUCCULENT, 6))
                  }
                  showAddHabitDialog = false
              }
          )
      }
      completedHabit?.let { habit ->
          AlertDialog(
              onDismissRequest = {},
              title = {
                  Text(
                      "Congratulations!",
                      modifier = Modifier.fillMaxWidth(),
                      textAlign = TextAlign.Center
                  )
              },
              text = {
                  Column(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                      Box(
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(190.dp)
                              .clip(RoundedCornerShape(28.dp))
                              .background(
                                  Brush.verticalGradient(
                                      listOf(Color(0xFFFFE7E8), Color(0xFFFFF3D9))
                                  )
                              ),
                          contentAlignment = Alignment.Center
                      ) {
                          Canvas(Modifier.fillMaxSize()) {
                              val sparkleColor = Color(0xFFFFC45C).copy(alpha = .8f)
                              listOf(
                                  Offset(size.width * .2f, size.height * .25f),
                                  Offset(size.width * .78f, size.height * .2f),
                                  Offset(size.width * .82f, size.height * .65f),
                                  Offset(size.width * .16f, size.height * .72f)
                              ).forEach { sparkle ->
                                  drawCircle(sparkleColor, radius = size.minDimension * .018f, center = sparkle)
                              }
                          }
                          PlantArtwork(
                              type = habit.plant,
                              habit = habit,
                              today = today,
                              modifier = Modifier.size(112.dp, 166.dp)
                          )
                      }
                      Text(
                          "Your habit's completed, you're ready for the next change in your life.",
                          modifier = Modifier.padding(top = GardenTokens.ContentInset),
                          textAlign = TextAlign.Center
                      )
                  }
              },
              confirmButton = {
                  TextButton(onClick = onCompletionAcknowledged) {
                      Text("Continue")
                  }
              }
          )
      }
    }
}
