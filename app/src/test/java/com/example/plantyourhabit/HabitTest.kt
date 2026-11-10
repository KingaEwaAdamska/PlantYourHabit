package com.example.plantyourhabit

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HabitTest {
    @Test
    fun exampleCareDaysEndsYesterday() {
        val today = LocalDate.now()

        val careDays = exampleCareDays(3).map(LocalDate::parse)

        assertEquals(3, careDays.size)
        assertEquals(today.minusDays(1), careDays.last())
        assertEquals(today.minusDays(3), careDays.first())
    }

    @Test
    fun addingTodayStartsStreakAndDoesNotDuplicateCareDay() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(1, "Water plants", PlantType.FERN, 5)

        val completed = habit.addToday(today).addToday(today)

        assertEquals(listOf(today.toString()), completed.careDays)
        assertEquals(1, completed.actualStreak(today))
    }

    @Test
    fun streakExpiresAfterMissingMoreThanOneDay() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(1, "Water plants", PlantType.FERN, 5, listOf(today.minusDays(2).toString()))

        assertEquals(0, habit.actualStreak(today))
        assertEquals(GrowthStage.WITHERING, habit.growthStage(today))
    }

    @Test
    fun removingTodayLeavesOriginalHabitUnchanged() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(1, "Water plants", PlantType.FERN, 5, listOf(today.toString()))

        val updated = habit.removeToday(today)

        assertEquals(listOf(today.toString()), habit.careDays)
        assertEquals(emptyList<String>(), updated.careDays)
        assertNotEquals(habit, updated)
    }

    @Test
    fun exampleCareDaysClampsValuesToSupportedRange() {
        assertTrue(exampleCareDays(-1).isEmpty())
        assertEquals(MAX_STREAK_DAY, exampleCareDays(MAX_STREAK_DAY + 1).size)
    }

    @Test
    fun futureCareDaysDoNotCountAsHistoryOrStreak() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            listOf(today.plusDays(1).toString())
        )

        assertFalse(habit.hasCareHistory(today))
        assertEquals(0, habit.actualStreak(today))
        assertEquals(GrowthStage.SEED, habit.growthStage(today))
    }

    @Test
    fun addingTodayAfterYesterdayContinuesStreak() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            listOf(today.minusDays(1).toString())
        )

        val updated = habit.addToday(today)

        assertEquals(2, updated.actualStreak(today))
        assertEquals(2, updated.careDays.size)
    }

    @Test
    fun addingTodayAfterGapResetsCareHistory() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            listOf(today.minusDays(2).toString(), today.minusDays(3).toString())
        )

        val updated = habit.addToday(today)

        assertEquals(listOf(today.toString()), updated.careDays)
        assertEquals(1, updated.actualStreak(today))
    }

    @Test
    fun addingTodayRemovesDuplicatesAndIgnoresFutureRecords() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            listOf(today.toString(), today.toString(), today.plusDays(1).toString())
        )

        assertEquals(listOf(today.toString()), habit.addToday(today).careDays)
    }

    @Test
    fun streakNeverExceedsMaximum() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = habitWithStreak(MAX_STREAK_DAY + 10, today)

        assertEquals(MAX_STREAK_DAY, habit.actualStreak(today))
    }

    @Test
    fun continuingLongStreakRetainsOnlyMostRecentDays() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            (1..MAX_STREAK_DAY).map { today.minusDays(it.toLong()).toString() }
        )

        val updated = habit.addToday(today)

        assertEquals(MAX_STREAK_DAY, updated.careDays.size)
        assertEquals(today.toString(), updated.careDays.last())
        assertEquals(today.minusDays(MAX_STREAK_DAY - 1L).toString(), updated.careDays.first())
    }

    @Test
    fun growthLabelsFollowStreakStageThresholds() {
        val today = LocalDate.of(2026, 10, 27)
        val cases = listOf(
            1 to GrowthStage.SEEDLING,
            GROWING_STAGE_START_DAY - 1 to GrowthStage.SEEDLING,
            GROWING_STAGE_START_DAY to GrowthStage.GROWING,
            BUDDING_STAGE_START_DAY - 1 to GrowthStage.GROWING,
            BUDDING_STAGE_START_DAY to GrowthStage.BUDDING,
            BLOOMING_STAGE_START_DAY - 1 to GrowthStage.BUDDING,
            BLOOMING_STAGE_START_DAY to GrowthStage.BLOOMING,
            MAX_STREAK_DAY - 1 to GrowthStage.BLOOMING,
            MAX_STREAK_DAY to GrowthStage.FULL_BLOOM
        )

        cases.forEach { (streak, expectedStage) ->
            assertEquals(expectedStage, habitWithStreak(streak, today).growthStage(today))
        }
    }

    @Test
    fun removingTodayPreservesOtherCareDays() {
        val today = LocalDate.of(2026, 10, 27)
        val yesterday = today.minusDays(1).toString()
        val habit = Habit(
            1,
            "Water plants",
            PlantType.FERN,
            5,
            listOf(yesterday, today.toString())
        )

        assertEquals(listOf(yesterday), habit.removeToday(today).careDays)
    }

    private fun habitWithStreak(streak: Int, today: LocalDate): Habit = Habit(
        id = 1,
        name = "Water plants",
        plant = PlantType.FERN,
        reward = 5,
        careDays = (0 until streak).map { today.minusDays(it.toLong()).toString() }
    )
}