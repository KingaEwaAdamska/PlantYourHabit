package com.example.plantyourhabit

import java.time.LocalDate

data class Habit(
    val id: Int,
    val name: String,
    val plant: String,
    val reward: Int,
    val careDays: List<String> = emptyList()
)

const val MAX_HABITS = 3
const val MAX_STREAK_DAY = 60
const val STARTING_GOLD_BALANCE = 120
const val GROWING_STAGE_START_DAY = 15
const val BUDDING_STAGE_START_DAY = 30
const val BLOOMING_STAGE_START_DAY = 45
const val NAV_SELECTION_ANIMATION_MILLIS = 400
const val NAV_SELECTION_MIDPOINT_MILLIS = 200
const val FLOWER_BLOOM_START_FRACTION = 0.75f
const val PLANT_GROWTH_ANIMATION_MILLIS = 500
const val GARDEN_DATE_REFRESH_MILLIS = 60_000L
const val PROGRESS_CIRCLE_DEGREES = 360f
const val GARDEN_WINDOW_FRAME_OPACITY = 0.75f
const val GARDEN_WINDOW_DECORATION_OPACITY = 0.55f

fun exampleCareDays(streakDays: Int): List<String> {
    val count = streakDays.coerceIn(0, MAX_STREAK_DAY)
    val today = LocalDate.now()
    return (count downTo 1)
        .map { daysAgo -> today.minusDays((daysAgo - 1).toLong()).toString() }
}

val initialHabits = listOf(
    Habit(1, "Morning water", "monstera", 6, exampleCareDays(1)),
    Habit(2, "Read 20 mins", "fern", 8, exampleCareDays(40)),
    Habit(3, "Evening walk", "sprout", 10)
)
