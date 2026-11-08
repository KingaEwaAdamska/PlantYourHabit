package com.example.plantyourhabit

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestTest {
    @Test
    fun availableTagsAreUniqueCaseInsensitivelyAndSorted() {
        val quests = listOf(
            quest(1, tags = listOf("Home", "health")),
            quest(2, tags = listOf("home", "Garden"))
        )

        assertEquals(listOf("Garden", "health", "Home"), availableQuestTags(quests))
    }

    @Test
    fun filteringCanHideCompletedQuests() {
        val quests = listOf(quest(1), quest(2), quest(3))

        val filtered = filterQuests(
            quests = quests,
            completedQuestIds = setOf(2),
            showCompleted = false,
            selectedTags = emptySet()
        )

        assertEquals(listOf(1, 3), filtered.map(Quest::id))
    }

    @Test
    fun selectedTagsMatchIgnoringCaseAndWhitespace() {
        val quests = listOf(
            quest(1, tags = listOf("Health")),
            quest(2, tags = listOf("Home"))
        )

        val filtered = filterQuests(
            quests = quests,
            completedQuestIds = emptySet(),
            showCompleted = true,
            selectedTags = setOf(" health ")
        )

        assertEquals(listOf(1), filtered.map(Quest::id))
    }

    @Test
    fun selectedTagsUseOrMatchingAndCompletedItemsSortLast() {
        val quests = listOf(
            quest(1, tags = listOf("Home")),
            quest(2, tags = listOf("Health")),
            quest(3, tags = listOf("Home", "Health"))
        )

        val filtered = filterQuests(
            quests = quests,
            completedQuestIds = setOf(1),
            showCompleted = true,
            selectedTags = setOf("Home", "Health")
        )

        assertEquals(listOf(2, 3, 1), filtered.map(Quest::id))
    }

    private fun quest(id: Int, tags: List<String> = emptyList()) = Quest(
        id = id,
        title = "Quest $id",
        description = "Description",
        tags = tags,
        difficulty = "Easy",
        reward = 10
    )
}
