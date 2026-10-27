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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.plantyourhabit.ui.theme.FlowerPalettes
import com.example.plantyourhabit.ui.theme.EveningGardenBackdrop
import com.example.plantyourhabit.ui.theme.EveningGardenSunset
import com.example.plantyourhabit.ui.theme.ForestGardenBackdrop
import com.example.plantyourhabit.ui.theme.PlantCardBorder
import com.example.plantyourhabit.ui.theme.ShelfPostWood
import com.example.plantyourhabit.ui.theme.ShelfPostHighlight
import com.example.plantyourhabit.ui.theme.ShelfPlankWood
import com.example.plantyourhabit.ui.theme.ShelfPlankHighlight
import com.example.plantyourhabit.ui.theme.GardenWindowFrame
import com.example.plantyourhabit.ui.theme.GardenWindowGlass
import com.example.plantyourhabit.ui.theme.GardenWindowSun
import com.example.plantyourhabit.ui.theme.BeigeLight
import com.example.plantyourhabit.ui.theme.LocalAppDarkTheme

@Composable
fun GardenScreen(
    habits: List<Habit>,
    ownedTrinkets: List<Int>,
    activeBackground: String,
    selectedPlants: Map<Int, String>,
    availablePlants: List<String>,
    onSelectPlant: (Int, String) -> Unit,
    onCreateHabit: (Habit) -> Unit,
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
                    habits = habits.take(MAX_HABITS),
                    today = today,
                    selectedPlants = selectedPlants,
                    onEditPlant = { editingHabitId = it },
                    onToggleHabit = onToggleHabit,
                    levelHeight = maxHeight / MAX_HABITS,
                    bottomShelfInset = GardenTokens.CardInset,
                    modifier = Modifier.fillMaxSize()
                )
                GardenTrinkets(
                    ownedTrinkets = ownedTrinkets,
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
                      onCreateHabit(Habit(nextId, name, "sprout", 6))
                  }
                  showAddHabitDialog = false
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
private fun GardenTrinkets(
    ownedTrinkets: List<Int>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        repeat(MAX_HABITS) { shelfIndex ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ownedTrinkets.getOrNull(shelfIndex)?.let { trinketId ->
                    val drawable = when (trinketId) {
                        1 -> R.drawable.ic_shop_mushroom
                        2 -> R.drawable.ic_shop_snail
                        3 -> R.drawable.ic_shop_lantern
                        else -> null
                    }
                    drawable?.let {
                        Image(
                            painter = painterResource(it),
                            contentDescription = when (trinketId) {
                                1 -> "Tiny mushroom trinket"
                                2 -> "Lucky snail trinket"
                                else -> "Golden lantern trinket"
                            },
                            modifier = Modifier
                                .align(
                                    if (shelfIndex % 2 == 0) {
                                        Alignment.BottomStart
                                    } else {
                                        Alignment.BottomEnd
                                    }
                                )
                                .offset(
                                    y = -(
                                        GardenTokens.ShelfPlankHeight +
                                            GardenTokens.CompactInset +
                                            if (shelfIndex == MAX_HABITS - 1) GardenTokens.CardInset
                                            else GardenTokens.NoElevation
                                        )
                                )
                                .padding(horizontal = GardenTokens.TrinketHorizontalInset)
                                .size(
                                    if (trinketId == 3) {
                                        GardenTokens.LanternDisplaySize
                                    } else {
                                        GardenTokens.TrinketDisplaySize
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
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
                habits.getOrNull(index)?.let { habit ->
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
private fun ShelfPlant(
    type: String,
    habit: Habit,
    today: LocalDate,
    modifier: Modifier = Modifier
) {
    val streak = habit.actualStreak(today)
    val animatedStage by animateFloatAsState(
        targetValue = streak.toFloat(),
        animationSpec = tween(durationMillis = PLANT_GROWTH_ANIMATION_MILLIS),
        label = "plantGrowth"
    )
    val growthGlow = remember(habit.id) { Animatable(0f) }
    var wasDone by remember(habit.id) {
        mutableStateOf(today.toString() in habit.careDays)
    }
    LaunchedEffect(today, habit.careDays) {
        val isDone = today.toString() in habit.careDays
        val justCompleted = isDone && !wasDone
        wasDone = isDone
        if (justCompleted) {
            growthGlow.animateTo(1f, animationSpec = tween(durationMillis = 180))
            growthGlow.animateTo(0f, animationSpec = tween(durationMillis = 650))
        }
    }
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val palette = FlowerPalettes.forPlant(type)
        val vitality = (animatedStage / MAX_STREAK_DAY).coerceIn(0f, 1f)
        val stem = lerp(palette.witheredStem, palette.stem, vitality)
        val leaf = lerp(palette.witheredLeaf, palette.leaf, vitality)

        fun drawLeaf(x: Float, y: Float, width: Float, height: Float, angle: Float, color: Color = leaf) {
            rotate(angle, pivot = androidx.compose.ui.geometry.Offset(w * x, h * y)) {
                drawOval(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(w * (x - width / 2), h * (y - height / 2)),
                    size = androidx.compose.ui.geometry.Size(w * width, h * height)
                )
                drawLine(
                    color = palette.leafVein.copy(alpha = .65f),
                    start = androidx.compose.ui.geometry.Offset(w * (x - width * .38f), h * y),
                    end = androidx.compose.ui.geometry.Offset(w * (x + width * .38f), h * y),
                    strokeWidth = GardenTokens.Hairline.toPx()
                )
            }
        }

        val plantScale = .68f + .32f * vitality
        withTransform({
            scale(plantScale, plantScale, pivot = androidx.compose.ui.geometry.Offset(w * .5f, h * .78f))
        }) {
        if (growthGlow.value > 0f) {
            val glowCenter = androidx.compose.ui.geometry.Offset(w * .5f, h * .43f)
            val glowRadius = minOf(w, h) * .58f
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFFFFD54F).copy(alpha = .72f * growthGlow.value),
                        .45f to Color(0xFFFFE89A).copy(alpha = .4f * growthGlow.value),
                        1f to Color.Transparent
                    ),
                    center = glowCenter,
                    radius = glowRadius
                ),
                radius = glowRadius,
                center = glowCenter
            )
        }
        if (animatedStage <= 0f) {
            if (habit.hasCareHistory(today)) {
                drawLine(
                    palette.witheredStem,
                    androidx.compose.ui.geometry.Offset(w * .5f, h * .78f),
                    androidx.compose.ui.geometry.Offset(w * .46f, h * .61f),
                    strokeWidth = w * .025f
                )
                drawOval(
                    palette.witheredLeaf,
                    androidx.compose.ui.geometry.Offset(w * .36f, h * .63f),
                    androidx.compose.ui.geometry.Size(w * .14f, h * .06f)
                )
                drawOval(
                    palette.witheredLeaf,
                    androidx.compose.ui.geometry.Offset(w * .48f, h * .66f),
                    androidx.compose.ui.geometry.Size(w * .14f, h * .06f)
                )
            } else {
                drawOval(
                    palette.seed,
                    androidx.compose.ui.geometry.Offset(w * .45f, h * .70f),
                    androidx.compose.ui.geometry.Size(w * .10f, h * .055f)
                )
            }
        } else when (type) {
            "cactus" -> {
                val body = roundRectShapePath(w, h)
                drawPath(body, FlowerPalettes.CactusDetails.body)
                drawPath(body, FlowerPalettes.CactusDetails.outline, style = Stroke(width = GardenTokens.TinyInset.toPx()))
                drawRoundRect(
                    color = FlowerPalettes.CactusDetails.highlight,
                    topLeft = androidx.compose.ui.geometry.Offset(w * .46f, h * .31f),
                    size = androidx.compose.ui.geometry.Size(w * .07f, h * .39f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * .04f)
                )
                drawRoundRect(
                    color = leaf,
                    topLeft = androidx.compose.ui.geometry.Offset(w * .23f, h * .46f),
                    size = androidx.compose.ui.geometry.Size(w * .16f, h * .24f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * .08f)
                )
                drawRoundRect(
                    color = leaf,
                    topLeft = androidx.compose.ui.geometry.Offset(w * .61f, h * .39f),
                    size = androidx.compose.ui.geometry.Size(w * .16f, h * .24f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * .08f)
                )
            }
            "bloom" -> {
                drawLine(stem, androidx.compose.ui.geometry.Offset(w * .5f, h * .79f), androidx.compose.ui.geometry.Offset(w * .5f, h * .28f), strokeWidth = w * .035f)
                drawLeaf(.34f, .56f, .32f, .14f, 18f)
                drawLeaf(.66f, .55f, .32f, .14f, -18f)
                val petals = listOf(
                    Triple(.5f, .23f, FlowerPalettes.BlossomDetails.blossomDark),
                    Triple(.40f, .27f, FlowerPalettes.BlossomDetails.blossom),
                    Triple(.60f, .27f, FlowerPalettes.BlossomDetails.blossomDark),
                    Triple(.45f, .17f, FlowerPalettes.BlossomDetails.blossomLight),
                    Triple(.55f, .17f, FlowerPalettes.BlossomDetails.blossom)
                )
                petals.forEach { (x, y, color) ->
                    drawOval(
                        color = color,
                        topLeft = androidx.compose.ui.geometry.Offset(w * (x - .09f), h * (y - .07f)),
                        size = androidx.compose.ui.geometry.Size(w * .18f, h * .14f)
                    )
                }
                drawCircle(
                    FlowerPalettes.BlossomDetails.center,
                    radius = w * .045f,
                    center = androidx.compose.ui.geometry.Offset(w * .5f, h * .23f)
                )
            }
            "lavender" -> {
                drawLine(stem, androidx.compose.ui.geometry.Offset(w * .5f, h * .79f), androidx.compose.ui.geometry.Offset(w * .5f, h * .20f), strokeWidth = w * .025f)
                drawLeaf(.35f, .58f, .28f, .08f, 12f)
                drawLeaf(.65f, .54f, .28f, .08f, -12f)
                for (i in 0..5) {
                    val y = .18f + i * .075f
                    val xOffset = if (i % 2 == 0) -.035f else .035f
                    drawCircle(
                        color = (if (i % 2 == 0) {
                            FlowerPalettes.LavenderDetails.lavenderDark
                        } else {
                            FlowerPalettes.LavenderDetails.lavenderLight
                        })
                            .copy(alpha = .3f + .7f * vitality),
                        radius = w * .065f,
                        center = androidx.compose.ui.geometry.Offset(w * (.5f + xOffset), h * y)
                    )
                }
            }
            "sprout" -> {
                drawLine(stem, androidx.compose.ui.geometry.Offset(w * .48f, h * .79f), androidx.compose.ui.geometry.Offset(w * .52f, h * .39f), strokeWidth = w * .035f)
                drawLeaf(.36f, .42f, .33f, .16f, -4f)
                drawLeaf(.63f, .33f, .33f, .16f, 3f)
            }
            "fern" -> {
                drawLine(stem, androidx.compose.ui.geometry.Offset(w * .5f, h * .79f), androidx.compose.ui.geometry.Offset(w * .5f, h * .24f), strokeWidth = w * .03f)
                listOf(
                    Triple(.32f, .58f, 14f), Triple(.68f, .52f, -14f),
                    Triple(.34f, .45f, 14f), Triple(.66f, .39f, -14f),
                    Triple(.39f, .32f, 14f), Triple(.61f, .27f, -14f)
                ).forEach { (x, y, angle) -> drawLeaf(x, y, .4f, .11f, angle) }
            }
            else -> {
                drawLine(stem, androidx.compose.ui.geometry.Offset(w * .5f, h * .79f), androidx.compose.ui.geometry.Offset(w * .5f, h * .24f), strokeWidth = w * .035f)
                drawLeaf(.31f, .59f, .40f, .20f, 18f)
                drawLeaf(.69f, .50f, .42f, .21f, -13f)
                drawLeaf(.36f, .36f, .34f, .18f, 27f)
                drawLeaf(.64f, .27f, .34f, .18f, -20f)
            }
        }
        val bloomStart = MAX_STREAK_DAY * FLOWER_BLOOM_START_FRACTION
        if (type != "bloom" && type != "lavender" && animatedStage >= bloomStart) {
            val bloomAlpha = ((animatedStage - bloomStart) /
                (MAX_STREAK_DAY * (1f - FLOWER_BLOOM_START_FRACTION))).coerceIn(0f, 1f)
            val center = androidx.compose.ui.geometry.Offset(w * .5f, h * .2f)
            listOf(
                androidx.compose.ui.geometry.Offset(0f, -1f),
                androidx.compose.ui.geometry.Offset(-.9f, -.3f),
                androidx.compose.ui.geometry.Offset(-.55f, .8f),
                androidx.compose.ui.geometry.Offset(.55f, .8f),
                androidx.compose.ui.geometry.Offset(.9f, -.3f)
            ).forEach { petal ->
                drawCircle(
                    color = FlowerPalettes.BlossomDetails.blossom.copy(alpha = bloomAlpha),
                    radius = w * .055f,
                    center = center + androidx.compose.ui.geometry.Offset(
                        petal.x * w * .07f,
                        petal.y * h * .045f
                    )
                )
            }
            drawCircle(
                color = FlowerPalettes.BlossomDetails.center.copy(alpha = bloomAlpha),
                radius = w * .025f,
                center = center
            )
        }
        }

        drawOval(
            color = palette.potShadow,
            topLeft = androidx.compose.ui.geometry.Offset(w * .28f, h * .76f),
            size = androidx.compose.ui.geometry.Size(w * .44f, h * .08f)
        )
        val pot = Path().apply {
            moveTo(w * .31f, h * .78f)
            lineTo(w * .69f, h * .78f)
            lineTo(w * .64f, h)
            lineTo(w * .36f, h)
            close()
        }
        drawPath(pot, palette.pot)
        drawRect(palette.potRim, androidx.compose.ui.geometry.Offset(w * .3f, h * .76f), androidx.compose.ui.geometry.Size(w * .4f, h * .045f))
    }
}

private fun roundRectShapePath(width: Float, height: Float) = Path().apply {
    addRoundRect(
        androidx.compose.ui.geometry.RoundRect(
            left = width * .37f,
            top = height * .28f,
            right = width * .63f,
            bottom = height * .78f,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(width * .13f)
        )
    )
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
        ShelfPlant(
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
                        ShelfPlant(
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
    "sprout" -> "Sprout"
    "bloom" -> "Coral blossom"
    "cactus" -> "Desert cactus"
    "lavender" -> "Soft lavender"
    else -> replaceFirstChar { it.uppercase() }
}

@Composable
private fun GardenWindow(activeBackground: String, modifier: Modifier = Modifier) {
    val windowShape = RoundedCornerShape(
        topStart = GardenTokens.GardenWindowInnerArchRadius,
        topEnd = GardenTokens.GardenWindowInnerArchRadius,
        bottomStart = GardenTokens.TinyInset,
        bottomEnd = GardenTokens.TinyInset
    )
    val windowSky = when (activeBackground) {
        "evening" -> Brush.verticalGradient(listOf(EveningGardenSunset, EveningGardenBackdrop))
        "forest" -> Brush.verticalGradient(listOf(Color(0xFFB9C9B1), Color(0xFF6F8B70)))
        else -> Brush.verticalGradient(listOf(GardenWindowGlass, GardenWindowGlass))
    }
    Box(
        modifier = modifier
            .size(width = GardenTokens.GardenWindowWidth, height = GardenTokens.GardenWindowHeight)
            .background(GardenWindowFrame.copy(alpha = GARDEN_WINDOW_FRAME_OPACITY), RoundedCornerShape(topStart = GardenTokens.GardenWindowArchRadius, topEnd = GardenTokens.GardenWindowArchRadius, bottomStart = GardenTokens.TightInset, bottomEnd = GardenTokens.TightInset))
            .padding(GardenTokens.MicroPadding)
            .clip(windowShape)
            .background(windowSky, windowShape)
    ) {
        if (activeBackground == "evening") {
            Canvas(Modifier.fillMaxSize()) {
                val sunCenter = Offset(size.width * 0.72f, size.height * 0.65f)
                drawCircle(
                    color = GardenWindowSun,
                    radius = size.minDimension * 0.14f,
                    center = sunCenter
                )
                val farHills = Path().apply {
                    moveTo(0f, size.height * 0.72f)
                    quadraticTo(size.width * 0.42f, size.height * 0.55f, size.width, size.height * 0.76f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(farHills, Color(0xFF9D8790).copy(alpha = 0.55f))
                val nearHills = Path().apply {
                    moveTo(0f, size.height * 0.84f)
                    quadraticTo(size.width * 0.45f, size.height * 0.68f, size.width, size.height * 0.88f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(nearHills, Color(0xFF688C5D).copy(alpha = 0.65f))
            }
        } else if (activeBackground == "forest") {
            Canvas(Modifier.fillMaxSize()) {
                val farForest = Path().apply {
                    moveTo(0f, size.height * 0.68f)
                    lineTo(size.width * 0.18f, size.height * 0.26f)
                    lineTo(size.width * 0.36f, size.height * 0.68f)
                    lineTo(size.width * 0.53f, size.height * 0.2f)
                    lineTo(size.width * 0.75f, size.height * 0.68f)
                    lineTo(size.width * 0.9f, size.height * 0.32f)
                    lineTo(size.width, size.height * 0.62f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(farForest, Color(0xFF789276).copy(alpha = 0.72f))

                val nearForest = Path().apply {
                    moveTo(0f, size.height * 0.82f)
                    lineTo(size.width * 0.13f, size.height * 0.45f)
                    lineTo(size.width * 0.27f, size.height * 0.82f)
                    lineTo(size.width * 0.43f, size.height * 0.4f)
                    lineTo(size.width * 0.61f, size.height * 0.82f)
                    lineTo(size.width * 0.79f, size.height * 0.46f)
                    lineTo(size.width * 0.96f, size.height * 0.82f)
                    lineTo(size.width, size.height * 0.76f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(nearForest, Color(0xFF496454).copy(alpha = 0.88f))
            }
        }
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .width(GardenTokens.CompactInset)
                .background(GardenWindowFrame)
        )
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(GardenTokens.CompactInset)
                .background(GardenWindowFrame)
        )
        if (activeBackground != "evening" && activeBackground != "forest") {
            Box(
                Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = GardenTokens.WindowSunTopInset,
                    end = GardenTokens.WindowSunEndInset
                )
                .size(GardenTokens.GardenWindowSunSize)
                .background(GardenWindowSun, CircleShape)
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = GardenTokens.LabelInset),
            horizontalArrangement = Arrangement.spacedBy(GardenTokens.TightInset)
        ) {
            repeat(2) {
                Box(Modifier.size(width = GardenTokens.HeaderInset, height = GardenTokens.SmallInset).background(Color.White.copy(alpha = GARDEN_WINDOW_DECORATION_OPACITY), CircleShape))
            }
        }
    }
}
