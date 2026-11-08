package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import java.time.LocalDate
import com.example.plantyourhabit.ui.theme.BeigeMedium
import com.example.plantyourhabit.ui.theme.SunroomGardenBackdrop
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.Green500
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green800
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextMuted
import com.example.plantyourhabit.ui.theme.EveningGardenBackdrop
import com.example.plantyourhabit.ui.theme.EveningGardenSunset
import com.example.plantyourhabit.ui.theme.ForestGardenBackdrop
import com.example.plantyourhabit.ui.theme.PlantCardBorder
import com.example.plantyourhabit.ui.theme.ShelfPostWood
import com.example.plantyourhabit.ui.theme.ShelfPostHighlight
import com.example.plantyourhabit.ui.theme.ShelfPlankWood
import com.example.plantyourhabit.ui.theme.ShelfPlankHighlight
import com.example.plantyourhabit.ui.theme.BeigeLight
import com.example.plantyourhabit.ui.theme.LocalAppDarkTheme

@Composable
fun GardenScreen(
    habits: List<Habit>,
    activeBackground: String,
    selectedPlants: Map<Int, String>,
    habitSlots: Map<Int, Int>,
    availablePlants: List<String>,
    onSelectPlant: (Int, String) -> Unit,
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
    val backdropBrush = when (activeBackground) {
        "sunroom" -> Brush.verticalGradient(listOf(SunroomGardenBackdrop, SunroomGardenBackdrop))
        "evening" -> Brush.verticalGradient(listOf(EveningGardenSunset, EveningGardenBackdrop))
        "forest" -> Brush.verticalGradient(listOf(ForestGardenBackdrop, ForestGardenBackdrop))
        else -> Brush.verticalGradient(listOf(Green300, Green300))
    }
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
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(backdropBrush)
            ) {
                GardenShelf(
                    backdrop = backdropBrush,
                    activeBackground = activeBackground,
                    modifier = Modifier.fillMaxSize()
                )
                GardenPlants(
                    habits = habits,
                    habitSlots = habitSlots,
                    today = today,
                    selectedPlants = selectedPlants,
                    onEditPlant = { editingHabitId = it },
                    onToggleHabit = onToggleHabit,
                    levelHeight = maxHeight / MAX_HABITS,
                    bottomShelfInset = GardenTokens.CardInset,
                    modifier = Modifier.fillMaxSize()
                )
            }
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
                      onCreateHabit(Habit(nextId, name, "succulent", 6))
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

@Composable
private fun AddHabitDialog(
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
private fun GardenShelf(
    backdrop: Brush,
    activeBackground: String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier.background(backdrop)) {
        val postTopPadding = maxHeight / MAX_HABITS - GardenTokens.ShelfPostInset
        GardenWindow(
            activeBackground = activeBackground,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = GardenTokens.CardInset, end = GardenTokens.LargeInset)
        )
        Column(Modifier.fillMaxSize()) {
            repeat(MAX_HABITS) { level ->
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    if (level < MAX_HABITS - 1) {
                        ShelfPlank(Modifier.align(Alignment.BottomCenter))
                    } else {
                        ShelfPlank(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = GardenTokens.CardInset)
                        )
                    }
                }
            }
        }
        ShelfPost(
            Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = GardenTokens.ShelfPostInset, top = postTopPadding - GardenTokens.ContentInset)
        )
        ShelfPost(
            Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = GardenTokens.ShelfPostInset, top = postTopPadding- GardenTokens.ContentInset)
        )
    }
}

@Composable
private fun GardenPlants(
    habits: List<Habit>,
    habitSlots: Map<Int, Int>,
    today: LocalDate,
    selectedPlants: Map<Int, String>,
    onEditPlant: (Int) -> Unit,
    onToggleHabit: (Habit) -> Unit,
    levelHeight: androidx.compose.ui.unit.Dp,
    bottomShelfInset: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        repeat(MAX_HABITS) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                habits.firstOrNull { habitSlots[it.id] == index }?.let { habit ->
                    val done = today.toString() in habit.careDays
                    val plankOffset = GardenTokens.ShelfPlankHeight +
                        if (index == MAX_HABITS - 1) bottomShelfInset else GardenTokens.NoElevation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = GardenTokens.PlantRowHorizontalInset),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (index % 2 == 0) {
                            ShelfHabitCard(
                                habit = habit,
                                done = done,
                                today = today,
                                onClick = { onToggleHabit(habit) },
                                modifier = Modifier.weight(1f)
                            )
                            GardenPlant(
                                selectedPlants[habit.id] ?: habit.plant,
                                habit = habit,
                                today = today,
                                modifier = Modifier
                                    .size(GardenTokens.ShelfPlantWidth, levelHeight)
                                    .offset(y = -plankOffset),
                                onEdit = { onEditPlant(habit.id) }
                            )
                        } else {
                            GardenPlant(
                                selectedPlants[habit.id] ?: habit.plant,
                                habit = habit,
                                today = today,
                                modifier = Modifier
                                    .size(GardenTokens.ShelfPlantWidth, levelHeight)
                                    .offset(y = -plankOffset),
                                onEdit = { onEditPlant(habit.id) }
                            )
                            ShelfHabitCard(
                                habit = habit,
                                done = done,
                                today = today,
                                onClick = { onToggleHabit(habit) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShelfHabitCard(
    habit: Habit,
    done: Boolean,
    today: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = LocalAppDarkTheme.current
    val habitCardBackground = if (isDarkTheme) Color(0xFF3A3D3F) else CreamSurface

    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = GardenTokens.SmallInset),
        shape = RoundedCornerShape(GardenTokens.HabitCardCornerRadius),
        color = habitCardBackground,
        shadowElevation = GardenTokens.TightInset
    ) {
        Row(
            modifier = Modifier.padding(horizontal = GardenTokens.CardContentInset, vertical = GardenTokens.GardenInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                android.util.Log.d(
                    "GardenStreak",
                    "name=${habit.name}, today=$today, " +
                            "careDays=${habit.careDays.size}, " +
                            "streak=${habit.actualStreak(today)}"
                )
                Text(
                    "${if (done) "CARED TODAY · " else ""}${habit.growthLabel(today)} · ${habit.actualStreak(today)}/$MAX_STREAK_DAY",
                    color = TextMuted,
                    fontSize = GardenTokens.TinyLabelText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = GardenTokens.HabitLetterSpacing
                )
                Text(
                    habit.name,
                    color = Green900,
                    fontSize = GardenTokens.CompactBodyText,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(GardenTokens.StandardInset))
            Surface(
                modifier = Modifier.size(GardenTokens.HabitCompletionControlSize),
                shape = CircleShape,
                color = if (done) Green700 else habitCardBackground,
                border = androidx.compose.foundation.BorderStroke(GardenTokens.Hairline, if (done) Green700 else PlantCardBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (done) {
                        androidx.compose.material3.Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = "Completed today",
                            tint = Color.White,
                            modifier = Modifier.size(GardenTokens.CheckmarkText.value.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ShelfPost(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(GardenTokens.GardenShelfPostWidth)
            .background(ShelfPostWood, RoundedCornerShape(GardenTokens.ShelfPostCornerRadius))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(GardenTokens.ShelfSupportWidth)
                .align(Alignment.CenterStart)
                .background(ShelfPostHighlight, RoundedCornerShape(GardenTokens.ShelfPostCornerRadius))
        )
    }
}

@Composable
private fun ShelfPlank(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = GardenTokens.StandardInset, vertical = GardenTokens.CompactInset)
            .fillMaxWidth()
            .height(GardenTokens.ShelfPlankHeight)
            .background(ShelfPlankWood, RoundedCornerShape(GardenTokens.ShelfPlankCornerRadius))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(GardenTokens.MicroPadding)
                .background(ShelfPlankHighlight, RoundedCornerShape(topStart = GardenTokens.ShelfPlankCornerRadius, topEnd = GardenTokens.ShelfPlankCornerRadius))
        )
    }
}

@Composable
private fun GardenPlant(
    type: String,
    habit: Habit,
    today: LocalDate,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {}
) {
    Box(modifier, contentAlignment = Alignment.BottomCenter) {
        PlantArtwork(
            type,
            habit,
            today,
            Modifier
                .fillMaxSize()
                .padding(start = GardenTokens.SmallInset, end = GardenTokens.SmallInset, top = GardenTokens.ContentInset)
        )
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = GardenTokens.PaletteButtonBottomInset)
                .size(GardenTokens.PaletteButtonSize)
                .clickable(onClick = onEdit),
            shape = CircleShape,
            color = CreamSurface,
            shadowElevation = GardenTokens.TinyInset
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_palette),
                    contentDescription = "Change plant",
                    tint = Green700,
                    modifier = Modifier.size(GardenTokens.PaletteIconSize)
                )
            }

        }
    }
}

@Composable
private fun PlantPickerDialog(
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
    "lavender" -> "Soft lavender"
    else -> replaceFirstChar { it.uppercase() }
}

@Composable
private fun GardenWindow(
    activeBackground: String,
    modifier: Modifier = Modifier
) {
    val windowImage = when (activeBackground) {
        "sunroom" -> R.drawable.window_sunroom
        "evening" -> R.drawable.window_evening
        "forest" -> R.drawable.window_forest
        else -> R.drawable.window_classic
    }
    Image(
        painter = painterResource(windowImage),
        contentDescription = null,
        modifier = modifier.size(
            width = GardenTokens.GardenWindowWidth,
            height = GardenTokens.GardenWindowHeight
        )
    )
}
