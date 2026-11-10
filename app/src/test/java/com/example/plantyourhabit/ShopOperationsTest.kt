package com.example.plantyourhabit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShopOperationsTest {
    @Test
    fun plantPurchaseDeductsGoldAndAddsOwnership() {
        val purchase = purchasePlant(
            gold = 100,
            ownedPlants = emptyList(),
            plant = PlantType.CACTUS,
            price = 85
        )

        assertEquals(15, purchase?.gold)
        assertEquals(listOf(PlantType.CACTUS), purchase?.ownedPlants)
    }

    @Test
    fun unaffordableOrDuplicatePlantPurchasesAreRejected() {
        assertNull(purchasePlant(84, emptyList(), PlantType.CACTUS, 85))
        assertNull(purchasePlant(100, listOf(PlantType.CACTUS), PlantType.CACTUS, 85))
    }

    @Test
    fun selectingOwnedThemeDoesNotCostGold() {
        val selection = selectGardenTheme(
            gold = 20,
            ownedThemes = listOf(GardenTheme.CLASSIC, GardenTheme.EVENING),
            theme = GardenTheme.EVENING
        )

        assertEquals(20, selection?.gold)
        assertEquals(GardenTheme.EVENING, selection?.activeTheme)
        assertEquals(listOf(GardenTheme.CLASSIC, GardenTheme.EVENING), selection?.ownedThemes)
    }

    @Test
    fun purchasingThemeDeductsGoldAndActivatesIt() {
        val selection = selectGardenTheme(
            gold = 150,
            ownedThemes = listOf(GardenTheme.CLASSIC),
            theme = GardenTheme.FOREST
        )

        assertEquals(0, selection?.gold)
        assertEquals(listOf(GardenTheme.CLASSIC, GardenTheme.FOREST), selection?.ownedThemes)
        assertEquals(GardenTheme.FOREST, selection?.activeTheme)
    }

    @Test
    fun unaffordableThemePurchaseIsRejected() {
        assertNull(
            selectGardenTheme(
                gold = 149,
                ownedThemes = listOf(GardenTheme.CLASSIC),
                theme = GardenTheme.FOREST
            )
        )
    }
}
