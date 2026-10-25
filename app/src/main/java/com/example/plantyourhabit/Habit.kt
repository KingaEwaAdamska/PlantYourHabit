
package com.example.plantyourhabit

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Habit(
    val id: Int,
    val name: String,
    val plant: String,
    val reward: Int,
    val careDays: List<String> = emptyList()
) {

    fun actualStreak(today: LocalDate = LocalDate.now()): Int {
        val recordedDays = careDays
            .map(LocalDate::parse)
            .filter { !it.isAfter(today) }
            .toSet()

        val lastCare = recordedDays.maxOrNull() ?: return 0

        // Streak wygasa, jeśli ostatnia aktywność była dawniej niż wczoraj.
        val daysSinceCare = ChronoUnit.DAYS.between(lastCare, today)
        if (daysSinceCare > 1) return 0

        var consecutiveDays = 0
        var date = lastCare

        while (date in recordedDays && consecutiveDays < MAX_STREAK_DAY) {
            consecutiveDays++
            date = date.minusDays(1)
        }

        return consecutiveDays.coerceAtMost(MAX_STREAK_DAY)
    }

    fun growthLabel(today: LocalDate = LocalDate.now()): String {
        return when {
            !hasCareHistory(today) -> "SEED"

            actualStreak(today) >= MAX_STREAK_DAY -> "FULL BLOOM"
            actualStreak(today) >= BLOOMING_STAGE_START_DAY -> "BLOOMING"
            actualStreak(today) >= BUDDING_STAGE_START_DAY -> "BUDDING"
            actualStreak(today) >= GROWING_STAGE_START_DAY -> "GROWING"
            actualStreak(today) > 0 -> "SEEDLING"

            else -> "WITHERING"
        }
    }

    fun hasCareHistory(today: LocalDate = LocalDate.now()): Boolean =
        careDays
            .map(LocalDate::parse)
            .any { !it.isAfter(today) }

    fun addToday(today: LocalDate = LocalDate.now()): Habit {
        val dates = careDays
            .map(LocalDate::parse)
            .filter { !it.isAfter(today) }
            .toSet()

        // Nie dodawaj tej samej daty drugi raz.
        if (today in dates) {
            return copy(careDays = dates.sorted().map(LocalDate::toString))
        }

        val continuesStreak = today.minusDays(1) in dates

        val updatedCareDays = if (continuesStreak) {
            (dates + today)
                .sorted()
                .takeLast(MAX_STREAK_DAY)
        } else {
            listOf(today)
        }

        return copy(
            careDays = updatedCareDays.map(LocalDate::toString)
        )
    }

    fun removeToday(today: LocalDate = LocalDate.now()): Habit =
        copy(
            careDays = careDays.filterNot { it == today.toString() }
        )
}

const val MAX_HABITS = 3
const val MAX_STREAK_DAY = 60
const val STARTING_GOLD_BALANCE = 920

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
        .map { daysAgo ->
            today.minusDays((daysAgo - 1).toLong()).toString()
        }
}

val initialHabits = listOf(
    Habit(1, "Morning water", "monstera", 6, exampleCareDays(1)),
    Habit(2, "Read 20 mins", "fern", 8, exampleCareDays(40)),
    Habit(3, "Evening walk", "sprout", 10, exampleCareDays(60))
)
