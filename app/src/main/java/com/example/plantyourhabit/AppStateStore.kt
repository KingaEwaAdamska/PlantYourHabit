package com.example.plantyourhabit

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

internal data class PersistedAppState(
    val gold: Int,
    val quests: List<Quest>,
    val completedQuestIds: Set<Int>,
    val ownedPlants: List<PlantType>,
    val ownedThemes: List<GardenTheme>,
    val activeTheme: GardenTheme
) {
    companion object {
        fun initial(): PersistedAppState {
            return PersistedAppState(
                gold = STARTING_GOLD_BALANCE,
                quests = initialQuests,
                completedQuestIds = emptySet(),
                ownedPlants = emptyList(),
                ownedThemes = listOf(GardenTheme.CLASSIC),
                activeTheme = GardenTheme.CLASSIC
            )
        }
    }
}

internal class AppStateStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun load(): PersistedAppState {
        val savedState = preferences.getString(STATE_KEY, null) ?: return PersistedAppState.initial()
        return decodeState(JSONObject(savedState))
    }

    fun save(state: PersistedAppState) {
        val saved = preferences.edit()
            .putString(STATE_KEY, encodeState(state).toString())
            .commit()
        check(saved) { "Failed to persist app state" }
    }

    private fun encodeState(state: PersistedAppState): JSONObject = JSONObject().apply {
        put("schemaVersion", STATE_SCHEMA_VERSION)
        put("gold", state.gold)
        put("quests", JSONArray().apply {
            state.quests.forEach { quest ->
                put(JSONObject().apply {
                    put("id", quest.id)
                    put("title", quest.title)
                    put("description", quest.description)
                    put("tags", JSONArray(quest.tags))
                    put("difficulty", quest.difficulty)
                    put("reward", quest.reward)
                })
            }
        })
        put("completedQuestIds", JSONArray().apply {
            state.completedQuestIds.forEach { put(it) }
        })
        put("ownedPlants", JSONArray().apply {
            state.ownedPlants.forEach { put(it.name) }
        })
        put("ownedBackgrounds", JSONArray(state.ownedThemes.map(GardenTheme::storageKey)))
        put("activeBackground", state.activeTheme.storageKey)
    }

    private fun decodeState(json: JSONObject): PersistedAppState {
        require(json.getInt("schemaVersion") == STATE_SCHEMA_VERSION) {
            "Unsupported app state schema version"
        }
        val quests = json.getJSONArray("quests").toObjectList { quest ->
            Quest(
                id = quest.getInt("id"),
                title = quest.getString("title"),
                description = quest.getString("description"),
                tags = quest.getJSONArray("tags").toStringList(),
                difficulty = quest.getString("difficulty"),
                reward = quest.getInt("reward")
            )
        }
        return PersistedAppState(
            gold = json.getInt("gold"),
            quests = quests,
            completedQuestIds = json.getJSONArray("completedQuestIds").toIntList().toSet(),
            ownedPlants = json.getJSONArray("ownedPlants").toStringList().map(PlantType::valueOf),
            ownedThemes = json.getJSONArray("ownedBackgrounds").toStringList()
                .map(GardenTheme::fromStorageKey),
            activeTheme = GardenTheme.fromStorageKey(json.getString("activeBackground"))
        )
    }

    private fun JSONArray.toStringList(): List<String> =
        List(length()) { index -> getString(index) }

    private fun JSONArray.toIntList(): List<Int> =
        List(length()) { index -> getInt(index) }

    private fun <T> JSONArray.toObjectList(transform: (JSONObject) -> T): List<T> =
        List(length()) { index -> transform(getJSONObject(index)) }

    private companion object {
        const val PREFERENCES_NAME = "plant_your_habit_state"
        const val STATE_KEY = "app_state"
        const val STATE_SCHEMA_VERSION = 1
    }
}
