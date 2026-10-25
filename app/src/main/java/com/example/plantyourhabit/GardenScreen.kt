package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
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
import androidx.compose.animation.core.tween
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.example.plantyourhabit.ui.theme.BeigeMedium
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

private data class PlantGrowth(
    val days: Int,
    val label: String,
    val hasHistory: Boolean
)

private fun plantGrowth(careDays: List<String>, today: LocalDate): PlantGrowth {
    if (careDays.isEmpty()) return PlantGrowth(0, "SEED", false)
    val recordedDays = careDays.map(LocalDate::parse).toSet()
    val latestCare = recordedDays.maxOrNull() ?: return PlantGrowth(0, "SEED", false)
    val daysSinceCare = ChronoUnit.DAYS.between(latestCare, today).toInt().coerceAtLeast(0)
    var streak = 0
    var date = latestCare
    while (date in recordedDays && streak < MAX_STREAK_DAY) {
        streak++
        date = date.minusDays(1)
    }
    val days = (streak - daysSinceCare).coerceIn(0, MAX_STREAK_DAY)
    val growthLabel = when {
        days >= MAX_STREAK_DAY -> "FULL BLOOM"
        days >= 45 -> "BLOOMING"
        days >= 30 -> "BUDDING"
        days >= 15 -> "GROWING"
        days > 0 -> "SEEDLING"
        else -> "WITHERING"
    }
    return PlantGrowth(days, growthLabel, true)
}

@Composable
fun GardenScreen(
    habits: List<Habit>,
    activeBackground: String,
    selectedPlants: Map<Int, String>,
    availablePlants: List<String>,
    onSelectPlant: (Int, String) -> Unit,
    onToggleHabit: (Habit) -> Unit,
    onOpenShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingHabitId by remember { mutableStateOf<Int?>(null) }
    var today by remember { mutableStateOf(LocalDate.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            today = LocalDate.now()
        }
    }
    val growthByHabit = habits.associate { habit ->
        habit.id to plantGrowth(habit.careDays, today)
    }
    val progress = habits.count { today.toString() in it.careDays }
    val backdrop = when (activeBackground) {
        "sunroom" -> BeigeMedium
        "evening" -> Color(0xFF9D8790)
        "forest" -> Green500
        else -> Green300
    }
    Box(modifier = modifier.fillMaxSize()) {
      Column(
          modifier = Modifier
              .fillMaxSize()
              .background(CreamBackground)
              .padding(horizontal = 11.dp, vertical = 5.dp)
      ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(CreamSurface, RoundedCornerShape(28.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Green800)
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (progress == habits.size) "Everything is blooming!" else "Your growing collection",
                            color = CreamBackground,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(40.dp)) {
                            drawArc(
                                color = Green300.copy(alpha = .35f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 3.dp.toPx())
                            )
                            drawArc(
                                color = Gold100,
                                startAngle = -90f,
                                sweepAngle = 360f * progress / habits.size,
                                useCenter = false,
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }
                        Text(
                            "$progress/${habits.size}",
                            color = CreamBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                }
                Text(
                    if (progress == habits.size) "All daily care finished" else "Care for your plants daily to build a streak up to $MAX_STREAK_DAY days.",
                    modifier = Modifier.padding(top = 4.dp),
                    color = Green200,
                    fontSize = 10.sp
                )
            }
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(backdrop)
            ) {
                GardenShelf(
                    backdrop = backdrop,
                    modifier = Modifier.fillMaxSize()
                )
                GardenPlants(
                    habits = habits.take(MAX_HABITS),
                    growthByHabit = growthByHabit,
                    today = today,
                    selectedPlants = selectedPlants,
                    onEditPlant = { editingHabitId = it },
                    onToggleHabit = onToggleHabit,
                    levelHeight = maxHeight / MAX_HABITS,
                    bottomShelfInset = 12.dp,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F1E5))
                    .padding(horizontal = 13.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (habits.size < MAX_HABITS) {
                    SoftActionButton(
                        text = "＋  Add a habit",
                        modifier = Modifier.weight(1f),
                        onClick = {}
                    )
                } else {
                    Text(
                        "You reached maximum amount of habits, perfect it before creating new ones",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 10.sp,
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
    }
}

@Composable
private fun GardenShelf(backdrop: Color, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.background(backdrop)) {
        val postTopPadding = maxHeight / MAX_HABITS - 19.dp
        GardenWindow(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 14.dp)
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
                                .padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
        ShelfPost(
            Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 19.dp, top = postTopPadding - 10.dp)
        )
        ShelfPost(
            Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 19.dp, top = postTopPadding- 10.dp)
        )
    }
}

@Composable
private fun GardenPlants(
    habits: List<Habit>,
    growthByHabit: Map<Int, PlantGrowth>,
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
                    val growth = growthByHabit[habit.id] ?: PlantGrowth(0, "SEED", false)
                    val done = today.toString() in habit.careDays
                    val plankOffset = 15.dp +
                        if (index == MAX_HABITS - 1) bottomShelfInset else 0.dp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (index % 2 == 0) {
                            ShelfHabitCard(
                                habit = habit,
                                done = done,
                                growth = growth,
                                onClick = { onToggleHabit(habit) },
                                modifier = Modifier.weight(1f)
                            )
                            GardenPlant(
                                selectedPlants[habit.id] ?: habit.plant,
                                growth = growth,
                                modifier = Modifier
                                    .size(105.dp, levelHeight)
                                    .offset(y = -plankOffset),
                                onEdit = { onEditPlant(habit.id) }
                            )
                        } else {
                            GardenPlant(
                                selectedPlants[habit.id] ?: habit.plant,
                                growth = growth,
                                modifier = Modifier
                                    .size(105.dp, levelHeight)
                                    .offset(y = -plankOffset),
                                onEdit = { onEditPlant(habit.id) }
                            )
                            ShelfHabitCard(
                                habit = habit,
                                done = done,
                                growth = growth,
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
    growth: PlantGrowth,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(17.dp),
        color = CreamSurface,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${if (done) "CARED TODAY · " else ""}${growth.label} · ${growth.days}/$MAX_STREAK_DAY",
                    color = TextMuted,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = .6.sp
                )
                Text(
                    habit.name,
                    color = Green900,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(27.dp),
                shape = CircleShape,
                color = if (done) Green700 else CreamSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (done) Green700 else Color(0xFFB9C4BA))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (done) Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ShelfPlant(
    type: String,
    growth: PlantGrowth,
    modifier: Modifier = Modifier
) {
    val animatedStage by animateFloatAsState(
        targetValue = growth.days.toFloat(),
        animationSpec = tween(durationMillis = 500),
        label = "plantGrowth"
    )
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
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        val plantScale = .68f + .32f * vitality
        withTransform({
            scale(plantScale, plantScale, pivot = androidx.compose.ui.geometry.Offset(w * .5f, h * .78f))
        }) {
        if (animatedStage <= 0f) {
            if (growth.hasHistory) {
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
                drawPath(body, palette.cactusBody)
                drawPath(body, palette.cactusOutline, style = Stroke(width = 2.dp.toPx()))
                drawRoundRect(
                    color = palette.cactusHighlight,
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
                    Triple(.5f, .23f, palette.blossomDark),
                    Triple(.40f, .27f, palette.blossom),
                    Triple(.60f, .27f, palette.blossomDark),
                    Triple(.45f, .17f, palette.blossomLight),
                    Triple(.55f, .17f, palette.blossom)
                )
                petals.forEach { (x, y, color) ->
                    drawOval(
                        color = color,
                        topLeft = androidx.compose.ui.geometry.Offset(w * (x - .09f), h * (y - .07f)),
                        size = androidx.compose.ui.geometry.Size(w * .18f, h * .14f)
                    )
                }
                drawCircle(
                    palette.blossomCenter,
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
                        color = (if (i % 2 == 0) palette.lavenderDark else palette.lavenderLight)
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
        if (type != "bloom" && type != "lavender" && animatedStage >= MAX_STREAK_DAY * .75f) {
            val bloomAlpha = ((animatedStage - MAX_STREAK_DAY * .75f) /
                (MAX_STREAK_DAY * .25f)).coerceIn(0f, 1f)
            val center = androidx.compose.ui.geometry.Offset(w * .5f, h * .2f)
            listOf(
                androidx.compose.ui.geometry.Offset(0f, -1f),
                androidx.compose.ui.geometry.Offset(-.9f, -.3f),
                androidx.compose.ui.geometry.Offset(-.55f, .8f),
                androidx.compose.ui.geometry.Offset(.55f, .8f),
                androidx.compose.ui.geometry.Offset(.9f, -.3f)
            ).forEach { petal ->
                drawCircle(
                    color = palette.blossom,
                    radius = w * .055f,
                    center = center + androidx.compose.ui.geometry.Offset(
                        petal.x * w * .07f,
                        petal.y * h * .045f
                    )
                )
            }
            drawCircle(
                color = palette.blossomCenter.copy(alpha = bloomAlpha),
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
            .width(15.dp)
            .background(Color(0xFF815438), RoundedCornerShape(5.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(6.dp)
                .align(Alignment.CenterStart)
                .background(Color(0xFFA56F48), RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun ShelfPlank(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(15.dp)
            .background(Color(0xFF865738), RoundedCornerShape(4.dp))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(Color(0xFFAE774D), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
    }
}

@Composable
private fun GardenPlant(
    type: String,
    growth: PlantGrowth,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {}
) {
    Box(modifier, contentAlignment = Alignment.BottomCenter) {
        ShelfPlant(
            type,
            growth,
            Modifier
                .fillMaxSize()
                .padding(start = 6.dp, end = 6.dp, top = 10.dp)
        )
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 1.dp)
                .size(27.dp)
                .clickable(onClick = onEdit),
            shape = CircleShape,
            color = CreamSurface,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_palette),
                    contentDescription = "Change plant",
                    tint = Green700,
                    modifier = Modifier.size(15.dp)
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
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Pick a flower for ${habit.name}.",
                    color = Green700,
                    fontSize = 13.sp
                )
                availablePlants.forEach { plant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (plant == selectedPlant) Green200 else CreamSurface)
                            .clickable { onSelect(plant) }
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShelfPlant(
                            plant,
                            growth = PlantGrowth(MAX_STREAK_DAY, "BLOOMING", true),
                            modifier = Modifier.size(38.dp, 48.dp)
                        )
                        Text(
                            plant.displayName(),
                            modifier = Modifier.weight(1f).padding(start = 10.dp),
                            color = Green900,
                            fontWeight = FontWeight.Medium
                        )
                        if (plant == selectedPlant) Text("✓", color = Green700)
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
private fun GardenWindow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 94.dp, height = 112.dp)
            .background(Color(0xFFEDF0DF).copy(alpha = .75f), RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp, bottomStart = 3.dp, bottomEnd = 3.dp))
            .padding(5.dp)
            .background(Color(0xFFB8D3D2), RoundedCornerShape(topStart = 43.dp, topEnd = 43.dp, bottomStart = 2.dp, bottomEnd = 2.dp))
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .width(4.dp)
                .background(Color(0xFFEDF0DF))
        )
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(4.dp)
                .background(Color(0xFFEDF0DF))
        )
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 13.dp, end = 9.dp)
                .size(24.dp)
                .background(Color(0xFFE8D58C), CircleShape)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(2) {
                Box(Modifier.size(width = 22.dp, height = 6.dp).background(Color.White.copy(alpha = .55f), CircleShape))
            }
        }
    }
}
