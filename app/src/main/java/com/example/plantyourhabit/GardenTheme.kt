package com.example.plantyourhabit

import androidx.compose.ui.graphics.Color

enum class GardenTheme(
    val storageKey: String,
    val displayName: String,
    val price: Int,
    val shopArtwork: Int,
    val previewTopColor: Color,
    val previewBottomColor: Color,
    val windowArtwork: Int
) {
    CLASSIC(
        storageKey = "classic",
        displayName = "Classic garden",
        price = 0,
        shopArtwork = R.drawable.ic_shop_leaf,
        previewTopColor = Color(0xFFAEC7B1),
        previewBottomColor = Color(0xFFAEC7B1),
        windowArtwork = R.drawable.window_classic
    ),
    SUNROOM(
        storageKey = "sunroom",
        displayName = "Warm sunroom",
        price = 90,
        shopArtwork = R.drawable.ic_shop_sun,
        previewTopColor = Color(0xFFE8D8B5),
        previewBottomColor = Color(0xFFE8D8B5),
        windowArtwork = R.drawable.window_sunroom
    ),
    EVENING(
        storageKey = "evening",
        displayName = "Quiet evening",
        price = 120,
        shopArtwork = R.drawable.ic_shop_moon,
        previewTopColor = Color(0xFFE6A16F),
        previewBottomColor = Color(0xFFD4C5D0),
        windowArtwork = R.drawable.window_evening
    ),
    FOREST(
        storageKey = "forest",
        displayName = "Forest nook",
        price = 150,
        shopArtwork = R.drawable.ic_shop_tree,
        previewTopColor = Color(0xFFB7C6A8),
        previewBottomColor = Color(0xFFB7C6A8),
        windowArtwork = R.drawable.window_forest
    );

    companion object {
        fun fromStorageKey(key: String): GardenTheme =
            entries.firstOrNull { it.storageKey == key }
                ?: throw IllegalArgumentException("Unknown garden theme key: $key")
    }
}
