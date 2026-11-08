package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDate
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.EveningGardenBackdrop
import com.example.plantyourhabit.ui.theme.EveningGardenSunset
import com.example.plantyourhabit.ui.theme.ForestGardenBackdrop
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.LocalAppDarkTheme
import com.example.plantyourhabit.ui.theme.PlantCardBorder
import com.example.plantyourhabit.ui.theme.ShelfPlankHighlight
import com.example.plantyourhabit.ui.theme.ShelfPlankWood
import com.example.plantyourhabit.ui.theme.ShelfPostHighlight
import com.example.plantyourhabit.ui.theme.ShelfPostWood
import com.example.plantyourhabit.ui.theme.SunroomGardenBackdrop
import com.example.plantyourhabit.ui.theme.TextMuted

@Composable
internal fun GardenRoom(
    habits: List<Habit>,
    activeBackground: String,
    selectedPlants: Map<Int, PlantType>,
    habitSlots: Map<Int, Int>,
    today: LocalDate,
    onEditPlant: (Int) -> Unit,
    onToggleHabit: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    val backdropBrush = when (activeBackground) {
        "sunroom" -> Brush.verticalGradient(listOf(SunroomGardenBackdrop, SunroomGardenBackdrop))
        "evening" -> Brush.verticalGradient(listOf(EveningGardenSunset, EveningGardenBackdrop))
        "forest" -> Brush.verticalGradient(listOf(ForestGardenBackdrop, ForestGardenBackdrop))
        else -> Brush.verticalGradient(listOf(Green300, Green300))
    }

    BoxWithConstraints(modifier.background(backdropBrush)) {
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
            onEditPlant = onEditPlant,
            onToggleHabit = onToggleHabit,
            levelHeight = maxHeight / MAX_HABITS,
            bottomShelfInset = GardenTokens.CardInset,
            modifier = Modifier.fillMaxSize()
        )
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
                .padding(end = GardenTokens.ShelfPostInset, top = postTopPadding - GardenTokens.ContentInset)
        )
    }
}

@Composable
private fun GardenPlants(
    habits: List<Habit>,
    habitSlots: Map<Int, Int>,
    today: LocalDate,
    selectedPlants: Map<Int, PlantType>,
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
                border = androidx.compose.foundation.BorderStroke(
                    GardenTokens.Hairline,
                    if (done) Green700 else PlantCardBorder
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (done) {
                        Icon(
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
                .background(
                    ShelfPlankHighlight,
                    RoundedCornerShape(
                        topStart = GardenTokens.ShelfPlankCornerRadius,
                        topEnd = GardenTokens.ShelfPlankCornerRadius
                    )
                )
        )
    }
}

@Composable
private fun GardenPlant(
    type: PlantType,
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
