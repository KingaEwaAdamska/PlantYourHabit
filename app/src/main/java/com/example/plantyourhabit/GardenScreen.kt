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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

private data class Habit(
    val id: Int,
    val name: String,
    val detail: String,
    val plant: String,
    val reward: Int
)

private val MAX_HABITS = 3

private val habits = listOf(
    Habit(1, "Morning water", "7 day streak", "monstera", 6),
    Habit(2, "Read 20 mins", "4 day streak", "fern", 8),
    Habit(3, "Evening walk", "Needs some care", "sprout", 10)
)

@Composable
fun GardenScreen(
    completedHabits: Set<Int>,
    activeBackground: String,
    onToggleHabit: (Int, Int) -> Unit,
    onOpenShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = completedHabits.size.coerceAtMost(habits.size)
    val backdrop = when (activeBackground) {
        "sunroom" -> BeigeMedium
        "evening" -> Color(0xFF9D8790)
        "forest" -> Green500
        else -> Green300
    }
    Column(
        modifier = modifier
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
                    if (progress == habits.size) "All daily care finished" else "Care for each plant, or make it feel more like yours.",
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
                    completedHabits = completedHabits,
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
    completedHabits: Set<Int>,
    onToggleHabit: (Int, Int) -> Unit,
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
                                done = habit.id in completedHabits,
                                onClick = { onToggleHabit(habit.id, habit.reward) },
                                modifier = Modifier.weight(1f)
                            )
                            GardenPlant(
                                habit.plant,
                                Modifier
                                    .size(105.dp, levelHeight)
                                    .offset(y = -plankOffset)
                            )
                        } else {
                            GardenPlant(
                                habit.plant,
                                Modifier
                                    .size(105.dp, levelHeight)
                                    .offset(y = -plankOffset)
                            )
                            ShelfHabitCard(
                                habit = habit,
                                done = habit.id in completedHabits,
                                onClick = { onToggleHabit(habit.id, habit.reward) },
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
                    if (done) "THRIVING TODAY" else habit.detail.uppercase(),
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
private fun ShelfPlant(type: String, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val stem = Color(0xFF496454)
        val leaf = when (type) {
            "fern" -> Color(0xFF718F5E)
            "sprout" -> Color(0xFF7C9069)
            else -> Color(0xFF527D54)
        }
        drawLine(stem, start = androidx.compose.ui.geometry.Offset(w * .5f, h * .79f), end = androidx.compose.ui.geometry.Offset(w * .5f, h * .24f), strokeWidth = w * .035f)
        val leaves = when (type) {
            "fern" -> listOf(
                Triple(.48f, .45f, -22f), Triple(.24f, .40f, 22f),
                Triple(.73f, .34f, -25f), Triple(.28f, .28f, 22f),
                Triple(.70f, .22f, -20f), Triple(.42f, .16f, 5f)
            )
            "sprout" -> listOf(
                Triple(.40f, .36f, 18f), Triple(.60f, .27f, -18f), Triple(.49f, .16f, 2f)
            )
            else -> listOf(
                Triple(.48f, .43f, -22f), Triple(.25f, .35f, 25f),
                Triple(.73f, .30f, -25f), Triple(.30f, .20f, 22f),
                Triple(.66f, .13f, -20f)
            )
        }
        leaves.forEach { (x, y, angle) ->
            val leafWidth = if (type == "sprout") .31f else .38f
            rotate(angle, pivot = androidx.compose.ui.geometry.Offset(w * x, h * y)) {
                drawOval(
                    color = leaf,
                    topLeft = androidx.compose.ui.geometry.Offset(w * (x - leafWidth / 2), h * (y - .055f)),
                    size = androidx.compose.ui.geometry.Size(w * leafWidth, h * .11f)
                )
                drawLine(
                    color = Green300,
                    start = androidx.compose.ui.geometry.Offset(w * (x - leafWidth * .42f), h * y),
                    end = androidx.compose.ui.geometry.Offset(w * (x + leafWidth * .42f), h * y),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
        drawOval(
            color = Color(0xFF8A5C3A),
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
        drawPath(pot, Color(0xFFB8734F))
        drawRect(Color(0xFFD89162), androidx.compose.ui.geometry.Offset(w * .3f, h * .76f), androidx.compose.ui.geometry.Size(w * .4f, h * .045f))
    }
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
private fun GardenPlant(type: String, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.BottomCenter) {
        ShelfPlant(
            type,
            Modifier
                .fillMaxSize()
                .padding(start = 6.dp, end = 6.dp, top = 10.dp)
        )
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 1.dp)
                .size(27.dp),
            shape = CircleShape,
            color = CreamSurface,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("◉", color = Green700, fontSize = 12.sp)
            }
        }
    }
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
