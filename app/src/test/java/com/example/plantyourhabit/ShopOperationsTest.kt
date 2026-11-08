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
    fun selectingOwnedBackgroundDoesNotCostGold() {
        val selection = selectGardenBackground(
            gold = 20,
            ownedBackgrounds = listOf("classic", "evening"),
            activeBackground = "classic",
            backgroundId = "evening",
            price = 120
        )

        assertEquals(20, selection?.gold)
        assertEquals("evening", selection?.activeBackground)
        assertEquals(listOf("classic", "evening"), selection?.ownedBackgrounds)
    }

    @Test
    fun purchasingBackgroundDeductsGoldAndActivatesIt() {
        val selection = selectGardenBackground(
            gold = 150,
            ownedBackgrounds = listOf("classic"),
            activeBackground = "classic",
            backgroundId = "forest",
            price = 150
        )

        assertEquals(0, selection?.gold)
        assertEquals(listOf("classic", "forest"), selection?.ownedBackgrounds)
        assertEquals("forest", selection?.activeBackground)
    }

    @Test
    fun unaffordableBackgroundPurchaseIsRejected() {
        assertNull(
            selectGardenBackground(
                gold = 149,
                ownedBackgrounds = listOf("classic"),
                activeBackground = "classic",
                backgroundId = "forest",
                price = 150
            )
        )
    }
}
