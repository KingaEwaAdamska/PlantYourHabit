package com.example.plantyourhabit

data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<String>,
    val difficulty: String,
    val reward: Int
)

val initialQuests = listOf(
    Quest(1, "Book dentist appointment", "Find a nearby clinic and choose a time that works.", listOf("Life admin", "Health"), "Medium", 35),
    Quest(2, "Organize the photo drawer", "Sort loose prints into keep, share, and archive piles.", listOf("Home"), "Medium", 25),
    Quest(3, "Try a new soup recipe", "Pick something seasonal and make enough for tomorrow.", listOf("Someday", "Cooking"), "Easy", 20),
    Quest(4, "Send the postcards", "Write a quick note and drop them in the mailbox.", listOf("Friends"), "Easy", 15)
)

fun availableQuestTags(quests: List<Quest>): List<String> =
    quests
        .flatMap(Quest::tags)
        .distinctBy { it.trim().lowercase() }
        .sortedBy { it.lowercase() }

fun filterQuests(
    quests: List<Quest>,
    completedQuestIds: Set<Int>,
    showCompleted: Boolean,
    selectedTags: Set<String>
): List<Quest> {
    val normalizedTags = selectedTags.map { it.trim().lowercase() }.toSet()
    return (if (normalizedTags.isEmpty()) {
        quests
    } else {
        quests.filter { quest ->
            quest.tags.any { it.trim().lowercase() in normalizedTags }
        }
    })
        .filter { showCompleted || it.id !in completedQuestIds }
        .sortedBy { it.id in completedQuestIds }
}
