package com.example.plantyourhabit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.plantyourhabit.ui.theme.PlantYourHabitTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppScreensComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val starterPlants = listOf(
        PlantType.MONSTERA,
        PlantType.FERN,
        PlantType.SUCCULENT
    )

    @Test
    fun gardenAddHabitDialogCreatesHabit() {
        val existingHabit = Habit(1, "Water plants", PlantType.FERN, 5)
        var createdHabit: Habit? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                GardenScreen(
                    habits = listOf(existingHabit),
                    activeBackground = GardenTheme.CLASSIC,
                    selectedPlants = mapOf(existingHabit.id to existingHabit.plant),
                    habitSlots = mapOf(existingHabit.id to 0),
                    availablePlants = starterPlants,
                    onSelectPlant = { _, _ -> },
                    onCreateHabit = { createdHabit = it },
                    completedHabit = null,
                    onCompletionAcknowledged = {},
                    onToggleHabit = {},
                    onOpenShop = {}
                )
            }
        }

        composeRule.onNodeWithText("Add a habit").performClick()
        composeRule.onNodeWithText("Habit name").performTextInput("Evening walk")
        composeRule.onNodeWithText("Add").performClick()

        composeRule.runOnIdle {
            assertEquals("Evening walk", createdHabit?.name)
            assertEquals(2, createdHabit?.id)
            assertEquals(PlantType.SUCCULENT, createdHabit?.plant)
        }
    }

    @Test
    fun tappingGardenHabitInvokesToggleCallback() {
        val habit = Habit(1, "Water plants", PlantType.FERN, 5)
        var toggledHabit: Habit? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                GardenScreen(
                    habits = listOf(habit),
                    activeBackground = GardenTheme.CLASSIC,
                    selectedPlants = mapOf(habit.id to habit.plant),
                    habitSlots = mapOf(habit.id to 0),
                    availablePlants = starterPlants,
                    onSelectPlant = { _, _ -> },
                    onCreateHabit = {},
                    completedHabit = null,
                    onCompletionAcknowledged = {},
                    onToggleHabit = { toggledHabit = it },
                    onOpenShop = {}
                )
            }
        }

        composeRule.onNodeWithText("Water plants").performClick()

        composeRule.runOnIdle {
            assertEquals(habit, toggledHabit)
        }
    }

    @Test
    fun questCompletionCheckboxInvokesCallback() {
        val quest = Quest(
            id = 12,
            title = "Read a chapter",
            description = "Read for twenty minutes.",
            tags = listOf("Reading"),
            difficulty = "Easy",
            reward = 20
        )
        var completedQuest: Pair<Int, Int>? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                QuestsScreen(
                    quests = listOf(quest),
                    completedQuests = emptySet(),
                    onCompleteQuest = { id, reward -> completedQuest = id to reward },
                    onCreateQuest = {},
                    onUpdateQuest = {}
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Complete Read a chapter")
            .performClick()

        composeRule.runOnIdle {
            assertEquals(quest.id to quest.reward, completedQuest)
        }
    }

    @Test
    fun shopPlantRowInvokesPurchaseCallback() {
        var purchase: Pair<PlantType, Int>? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                ShopScreen(
                    gold = 100,
                    ownedPlants = emptyList(),
                    ownedThemes = listOf(GardenTheme.CLASSIC),
                    activeTheme = GardenTheme.CLASSIC,
                    onBuyPlant = { plant, price -> purchase = plant to price },
                    onSelectTheme = {}
                )
            }
        }

        composeRule.onNodeWithText("Desert cactus").performClick()

        composeRule.runOnIdle {
            assertEquals(PlantType.CACTUS to 85, purchase)
        }
    }

    @Test
    fun shopBackgroundRowInvokesSelectionCallback() {
        var selection: GardenTheme? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                ShopScreen(
                    gold = 100,
                    ownedPlants = emptyList(),
                    ownedThemes = listOf(GardenTheme.CLASSIC),
                    activeTheme = GardenTheme.CLASSIC,
                    onBuyPlant = { _, _ -> },
                    onSelectTheme = { theme -> selection = theme }
                )
            }
        }

        composeRule.onNodeWithText("Warm sunroom")
            .performScrollTo()
            .performClick()

        composeRule.runOnIdle {
            assertEquals(GardenTheme.SUNROOM, selection)
        }
    }

    @Test
    fun questEditorCreatesQuestFromEnteredDetails() {
        var createdQuest: Quest? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                QuestsScreen(
                    quests = listOf(quest(1, "Existing quest")),
                    completedQuests = emptySet(),
                    onCompleteQuest = { _, _ -> },
                    onCreateQuest = { createdQuest = it },
                    onUpdateQuest = {}
                )
            }
        }

        composeRule.onNodeWithText("Add a sidequest").performClick()
        composeRule.onNodeWithText("Quest title").performTextInput("Plan a trip")
        composeRule.onNodeWithText("Description").performTextInput("Choose a destination")
        composeRule.onNodeWithText("Tags (comma separated)").performTextInput("Travel, Fun")
        composeRule.onNodeWithText("Create").performClick()

        composeRule.runOnIdle {
            assertEquals("Plan a trip", createdQuest?.title)
            assertEquals("Choose a destination", createdQuest?.description)
            assertEquals(listOf("Travel", "Fun"), createdQuest?.tags)
            assertEquals(2, createdQuest?.id)
        }
    }

    @Test
    fun questEditorSavesEditedTitle() {
        val originalQuest = quest(1, "Tidy desk")
        var updatedQuest: Quest? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                QuestsScreen(
                    quests = listOf(originalQuest),
                    completedQuests = emptySet(),
                    onCompleteQuest = { _, _ -> },
                    onCreateQuest = {},
                    onUpdateQuest = { updatedQuest = it }
                )
            }
        }

        composeRule.onNodeWithText("Tidy desk").performClick()
        composeRule.onNodeWithText("Quest title").performTextClearance()
        composeRule.onNodeWithText("Quest title").performTextInput("Organize desk")
        composeRule.onNodeWithText("Save").performClick()

        composeRule.runOnIdle {
            assertEquals("Organize desk", updatedQuest?.title)
            assertEquals(originalQuest.id, updatedQuest?.id)
        }
    }

    @Test
    fun completedQuestCheckboxIsDisabled() {
        val quest = quest(7, "Completed quest")
        composeRule.setContent {
            PlantYourHabitTheme {
                QuestsScreen(
                    quests = listOf(quest),
                    completedQuests = setOf(quest.id),
                    onCompleteQuest = { _, _ -> },
                    onCreateQuest = {},
                    onUpdateQuest = {}
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Complete Completed quest")
            .assertIsNotEnabled()
    }

    @Test
    fun shopPlantPurchaseIsDisabledWithoutEnoughGold() {
        composeRule.setContent {
            PlantYourHabitTheme {
                ShopScreen(
                    gold = 10,
                    ownedPlants = emptyList(),
                    ownedThemes = listOf(GardenTheme.CLASSIC),
                    activeTheme = GardenTheme.CLASSIC,
                    onBuyPlant = { _, _ -> },
                    onSelectTheme = {}
                )
            }
        }

        composeRule.onNodeWithText("Desert cactus").assertIsNotEnabled()
    }

    @Test
    fun gardenPlantPickerSelectsAvailablePlant() {
        val habit = Habit(1, "Water plants", PlantType.FERN, 5)
        var selectedPlant: Pair<Int, PlantType>? = null
        composeRule.setContent {
            PlantYourHabitTheme {
                GardenScreen(
                    habits = listOf(habit),
                    activeBackground = GardenTheme.CLASSIC,
                    selectedPlants = mapOf(habit.id to habit.plant),
                    habitSlots = mapOf(habit.id to 0),
                    availablePlants = starterPlants + PlantType.CACTUS,
                    onSelectPlant = { id, plant -> selectedPlant = id to plant },
                    onCreateHabit = {},
                    completedHabit = null,
                    onCompletionAcknowledged = {},
                    onToggleHabit = {},
                    onOpenShop = {}
                )
            }
        }

        composeRule.onNodeWithContentDescription("Change plant").performClick()
        composeRule.onNodeWithText("Desert cactus").performClick()

        composeRule.runOnIdle {
            assertEquals(habit.id to PlantType.CACTUS, selectedPlant)
        }
    }

    private fun quest(id: Int, title: String) = Quest(
        id = id,
        title = title,
        description = "A short description",
        tags = listOf("Home"),
        difficulty = "Easy",
        reward = 20
    )
}
