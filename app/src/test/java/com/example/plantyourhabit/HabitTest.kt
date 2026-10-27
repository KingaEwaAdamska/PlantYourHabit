package com.example.plantyourhabit

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
        val habit = Habit(1, "Water plants", "fern", 5)

        val completed = habit.addToday(today).addToday(today)

        assertEquals(listOf(today.toString()), completed.careDays)
        assertEquals(1, completed.actualStreak(today))
    }

    @Test
    fun streakExpiresAfterMissingMoreThanOneDay() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(1, "Water plants", "fern", 5, listOf(today.minusDays(2).toString()))

        assertEquals(0, habit.actualStreak(today))
        assertEquals("WITHERING", habit.growthLabel(today))
    }

    @Test
    fun removingTodayLeavesOriginalHabitUnchanged() {
        val today = LocalDate.of(2026, 10, 27)
        val habit = Habit(1, "Water plants", "fern", 5, listOf(today.toString()))

        val updated = habit.removeToday(today)

        assertEquals(listOf(today.toString()), habit.careDays)
        assertEquals(emptyList<String>(), updated.careDays)
        assertNotEquals(habit, updated)
    }
}