package com.example.plantyourhabit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.plantyourhabit.ui.theme.FlowerPalettes
import java.time.LocalDate

@Composable
internal fun PlantArtwork(
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

    val vitality = (animatedStage / MAX_STREAK_DAY).coerceIn(0f, 1f)
    val plantScale = .68f + .32f * vitality
    val plantColorFilter = remember(type, vitality) { vitalityColorFilter(type, vitality) }
    val plantModifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            scaleX = if (type == "fern") 1.25f else plantScale
            scaleY = plantScale
            transformOrigin = TransformOrigin(.5f, .78f)
        }

    Box(modifier, contentAlignment = Alignment.BottomCenter) {
        Canvas(Modifier.fillMaxSize()) {
            if (growthGlow.value > 0f) {
                val glowCenter = Offset(size.width * .5f, size.height * .43f)
                val glowRadius = minOf(size.width, size.height) * .58f
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
        }

        if (type == "monstera" && animatedStage > 0f) {
            PlantPot(type)
        }

        val plantResource = if (animatedStage <= 0f) {
            if (habit.hasCareHistory(today)) R.drawable.ic_plant_withered else R.drawable.ic_plant_seed
        } else {
            plantDrawable(type)
        }
        Image(
            painter = painterResource(plantResource),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            colorFilter = if (animatedStage > 0f) plantColorFilter else null,
            modifier = plantModifier
        )

        val bloomStart = MAX_STREAK_DAY * FLOWER_BLOOM_START_FRACTION
        if (type != "chinese_money_plant" && type != "succulent" && type != "monstera" && type != "fern" && animatedStage >= bloomStart) {
            val bloomAlpha = ((animatedStage - bloomStart) /
                (MAX_STREAK_DAY * (1f - FLOWER_BLOOM_START_FRACTION))).coerceIn(0f, 1f)
            Image(
                painter = painterResource(
                    when (type) {
                        "cactus" -> R.drawable.ic_plant_cactus_bloom
                        "orchid" -> R.drawable.ic_plant_orchid_head
                        else -> R.drawable.ic_plant_mature_bloom
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = plantModifier.graphicsLayer { alpha = bloomAlpha }
            )
        }

        if (type != "monstera" || animatedStage <= 0f) {
            PlantPot(type)
        }
    }
}

@Composable
private fun PlantPot(type: String) {
    Image(
        painter = painterResource(potDrawable(type)),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}

private fun plantDrawable(type: String): Int = when (type) {
    "cactus" -> R.drawable.ic_plant_cactus
    "chinese_money_plant" -> R.drawable.ic_plant_chinese_money_plant
    "orchid" -> R.drawable.ic_plant_orchid
    "monstera" -> R.drawable.ic_plant_monstera
    "succulent" -> R.drawable.ic_plant_succulent
    "fern" -> R.drawable.ic_plant_fern
    else -> R.drawable.ic_plant_generic
}

private fun potDrawable(type: String): Int = when (type) {
    "monstera" -> R.drawable.ic_plant_pot_monstera
    "fern" -> R.drawable.ic_plant_pot_fern
    else -> R.drawable.ic_plant_pot_terracotta
}

private fun vitalityColorFilter(type: String, vitality: Float): ColorFilter {
    val palette = FlowerPalettes.forPlant(type)
    fun channelScale(healthy: Float, withered: Float): Float =
        1f + ((withered / healthy) - 1f) * (1f - vitality)

    val matrix = ColorMatrix(
        floatArrayOf(
            channelScale(palette.leaf.red, palette.witheredLeaf.red), 0f, 0f, 0f, 0f,
            0f, channelScale(palette.leaf.green, palette.witheredLeaf.green), 0f, 0f, 0f,
            0f, 0f, channelScale(palette.leaf.blue, palette.witheredLeaf.blue), 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    return ColorFilter.colorMatrix(matrix)
}
