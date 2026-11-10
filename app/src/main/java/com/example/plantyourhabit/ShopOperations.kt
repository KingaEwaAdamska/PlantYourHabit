package com.example.plantyourhabit

data class PlantShopState(
    val gold: Int,
    val ownedPlants: List<PlantType>
)

data class GardenShopState(
    val gold: Int,
    val ownedThemes: List<GardenTheme>,
    val activeTheme: GardenTheme
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

fun selectGardenTheme(
    gold: Int,
    ownedThemes: List<GardenTheme>,
    theme: GardenTheme
): GardenShopState? {
    if (theme in ownedThemes) {
        return GardenShopState(gold, ownedThemes, theme)
    }
    if (gold < theme.price) return null
    return GardenShopState(
        gold = gold - theme.price,
        ownedThemes = ownedThemes + theme,
        activeTheme = theme
    )
}
