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
