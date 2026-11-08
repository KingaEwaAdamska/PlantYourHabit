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
    type: PlantType,
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
    val plantScale = plantGrowthScale(type, vitality)
    val plantColorFilter = remember(type, vitality) { vitalityColorFilter(type, vitality) }
    val plantModifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            scaleX = plantScale.scaleX
            scaleY = plantScale.scaleY
            transformOrigin = TransformOrigin(.5f, .78f)
        }
    val renderState = PlantRenderState(
        type = type,
        habit = habit,
        today = today,
        animatedStage = animatedStage,
        colorFilter = plantColorFilter,
        modifier = plantModifier
    )

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

        PlantScene(renderState)
    }
}

private data class PlantRenderState(
    val type: PlantType,
    val habit: Habit,
    val today: LocalDate,
    val animatedStage: Float,
    val colorFilter: ColorFilter,
    val modifier: Modifier
)

@Composable
private fun PlantScene(state: PlantRenderState) {
    when (state.type) {
        PlantType.CACTUS -> CactusArtwork(state)
        PlantType.CHINESE_MONEY_PLANT -> ChineseMoneyPlantArtwork(state)
        PlantType.FERN -> FernArtwork(state)
        PlantType.MONSTERA -> MonsteraArtwork(state)
        PlantType.ORCHID -> OrchidArtwork(state)
        PlantType.SUCCULENT -> SucculentArtwork(state)
    }
}

@Composable
private fun CactusArtwork(state: PlantRenderState) {
    PlantBody(state, R.drawable.ic_plant_cactus)
    CactusBloom(state)
    PlantPot(PlantType.CACTUS)
}

@Composable
private fun ChineseMoneyPlantArtwork(state: PlantRenderState) {
    PlantBody(state, R.drawable.ic_plant_chinese_money_plant)
    PlantPot(PlantType.CHINESE_MONEY_PLANT)
}

@Composable
private fun FernArtwork(state: PlantRenderState) {
    PlantBody(state, R.drawable.ic_plant_fern)
    PlantPot(PlantType.FERN)
}

@Composable
private fun MonsteraArtwork(state: PlantRenderState) {
    if (state.animatedStage > 0f) {
        PlantPot(PlantType.MONSTERA)
    }
    PlantBody(state, R.drawable.ic_plant_monstera)
    if (state.animatedStage <= 0f) {
        PlantPot(PlantType.MONSTERA)
    }
}

@Composable
private fun OrchidArtwork(state: PlantRenderState) {
    PlantBody(state, R.drawable.ic_plant_orchid)
    OrchidBloom(state)
    PlantPot(PlantType.ORCHID)
}

@Composable
private fun SucculentArtwork(state: PlantRenderState) {
    PlantBody(state, R.drawable.ic_plant_succulent)
    PlantPot(PlantType.SUCCULENT)
}

@Composable
private fun PlantBody(state: PlantRenderState, drawable: Int) {
    val resource = if (state.animatedStage <= 0f) {
        if (state.habit.hasCareHistory(state.today)) {
            R.drawable.ic_plant_withered
        } else {
            R.drawable.ic_plant_seed
        }
    } else {
        drawable
    }
    Image(
        painter = painterResource(resource),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        colorFilter = if (state.animatedStage > 0f) state.colorFilter else null,
        modifier = state.modifier
    )
}

@Composable
private fun CactusBloom(state: PlantRenderState) {
    MatureBloom(state, R.drawable.ic_plant_cactus_bloom)
}

@Composable
private fun OrchidBloom(state: PlantRenderState) {
    MatureBloom(state, R.drawable.ic_plant_orchid_head)
}

@Composable
private fun MatureBloom(state: PlantRenderState, drawable: Int) {
    val bloomStart = MAX_STREAK_DAY * FLOWER_BLOOM_START_FRACTION
    if (state.animatedStage < bloomStart) return

    val bloomAlpha = ((state.animatedStage - bloomStart) /
        (MAX_STREAK_DAY * (1f - FLOWER_BLOOM_START_FRACTION))).coerceIn(0f, 1f)
    Image(
        painter = painterResource(drawable),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = state.modifier.graphicsLayer { alpha = bloomAlpha }
    )
}

@Composable
private fun PlantPot(type: PlantType) {
    Image(
        painter = painterResource(potDrawable(type)),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}

private data class PlantGrowthScale(
    val scaleX: Float,
    val scaleY: Float
)

private fun plantGrowthScale(type: PlantType, vitality: Float): PlantGrowthScale = when (type) {
    PlantType.CACTUS -> cactusGrowthScale(vitality)
    PlantType.CHINESE_MONEY_PLANT -> chineseMoneyPlantGrowthScale(vitality)
    PlantType.FERN -> fernGrowthScale(vitality)
    PlantType.MONSTERA -> monsteraGrowthScale(vitality)
    PlantType.ORCHID -> orchidGrowthScale(vitality)
    PlantType.SUCCULENT -> succulentGrowthScale(vitality)
}

private fun cactusGrowthScale(vitality: Float) = uniformGrowthScale(vitality)

private fun chineseMoneyPlantGrowthScale(vitality: Float) = uniformGrowthScale(vitality)

private fun fernGrowthScale(vitality: Float) = PlantGrowthScale(
    scaleX = 1.25f,
    scaleY = uniformGrowthScale(vitality).scaleY
)

private fun monsteraGrowthScale(vitality: Float) = uniformGrowthScale(vitality)

private fun orchidGrowthScale(vitality: Float) = uniformGrowthScale(vitality)

private fun succulentGrowthScale(vitality: Float) = uniformGrowthScale(vitality)

private fun uniformGrowthScale(vitality: Float): PlantGrowthScale {
    val scale = .68f + .32f * vitality
    return PlantGrowthScale(scaleX = scale, scaleY = scale)
}

private fun potDrawable(type: PlantType): Int = when (type) {
    PlantType.MONSTERA -> R.drawable.ic_plant_pot_monstera
    PlantType.FERN -> R.drawable.ic_plant_pot_fern
    else -> R.drawable.ic_plant_pot_terracotta
}

private fun vitalityColorFilter(type: PlantType, vitality: Float): ColorFilter {
    val palette = FlowerPalettes.forPlant(type)
    fun channelScale(young: Float, mature: Float): Float =
        1f + ((young / mature) - 1f) * (1f - vitality)

    val matrix = ColorMatrix(
        floatArrayOf(
            channelScale(palette.youngLeaf.red, palette.leaf.red), 0f, 0f, 0f, 0f,
            0f, channelScale(palette.youngLeaf.green, palette.leaf.green), 0f, 0f, 0f,
            0f, 0f, channelScale(palette.youngLeaf.blue, palette.leaf.blue), 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    return ColorFilter.colorMatrix(matrix)
}
