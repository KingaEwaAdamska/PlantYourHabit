package com.example.plantyourhabit

data class PlantShopState(
    val gold: Int,
    val ownedPlants: List<PlantType>
)

data class GardenShopState(
    val gold: Int,
    val ownedBackgrounds: List<String>,
    val activeBackground: String
)

fun purchasePlant(
    gold: Int,
    ownedPlants: List<PlantType>,
    plant: PlantType,
    price: Int
): PlantShopState? {
    require(price >= 0) { "Plant price cannot be negative" }
    if (plant in ownedPlants || gold < price) return null
    return PlantShopState(
        gold = gold - price,
        ownedPlants = ownedPlants + plant
    )
}

fun selectGardenBackground(
    gold: Int,
    ownedBackgrounds: List<String>,
    activeBackground: String,
    backgroundId: String,
    price: Int
): GardenShopState? {
    require(price >= 0) { "Background price cannot be negative" }
    if (backgroundId in ownedBackgrounds) {
        return GardenShopState(gold, ownedBackgrounds, backgroundId)
    }
    if (gold < price) return null
    return GardenShopState(
        gold = gold - price,
        ownedBackgrounds = ownedBackgrounds + backgroundId,
        activeBackground = backgroundId
    )
}
