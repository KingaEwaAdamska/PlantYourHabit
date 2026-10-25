package com.example.plantyourhabit.ui.theme

import androidx.compose.ui.graphics.Color

// Primary — naturalna, leśna zieleń
val Green900 = Color(0xFF26392F)
val Green800 = Color(0xFF355947)
val Green700 = Color(0xFF496454)
val Green600 = Color(0xFF527560)
val Green500 = Color(0xFF688C5D)
val Green300 = Color(0xFFAEC7B1)
val Green200 = Color(0xFFDCE8D9)
val Green100 = Color(0xFFEDF2E8)

// Background — ciepłe, naturalne tło
val CreamBackground = Color(0xFFF8F5ED)
val CreamSurface = Color(0xFFFFFDF8)
val BeigeBackground = Color(0xFFE7E1D3)
val BeigeLight = Color(0xFFF6F1E5)
val BeigeMedium = Color(0xFFE8D8B5)

// Accent — złoto i miód
val Gold700 = Color(0xFF8D6424)
val Gold600 = Color(0xFF99722D)
val Gold400 = Color(0xFFEAB866)
val Gold300 = Color(0xFFF6D57C)
val Gold100 = Color(0xFFF7E6B4)

// Terracotta — doniczki i dekoracje
val Terracotta700 = Color(0xFFA75D45)
val Terracotta600 = Color(0xFFB8634D)
val Terracotta500 = Color(0xFFC47F57)
val Terracotta300 = Color(0xFFD89162)
val Terracotta100 = Color(0xFFF3DDD5)

// Text — teksty i elementy pomocnicze
val TextPrimary = Color(0xFF26392F)
val TextSecondary = Color(0xFF66766B)
val TextMuted = Color(0xFF909A91)
val BorderColor = Color(0xFFE3E2D9)
val DividerColor = Color(0xFFE7E5DD)

// Flowers — dodatkowe akcenty
val FlowerPink = Color(0xFFD88975)
val Lavender = Color(0xFF9A81AE)
val LeafGreen = Color(0xFF7C9069)
val EveningGardenBackdrop = Color(0xFFD4C5D0)
val EveningGardenSunset = Color(0xFFE6A16F)
val ForestGardenBackdrop = Color(0xFFB7C6A8)
val PlantCardBorder = Color(0xFFB9C4BA)
val ShelfPostWood = Color(0xFF815438)
val ShelfPostHighlight = Color(0xFFA56F48)
val ShelfPlankWood = Color(0xFF865738)
val ShelfPlankHighlight = Color(0xFFAE774D)
val GardenWindowFrame = Color(0xFFEDF0DF)
val GardenWindowGlass = Color(0xFFB8D3D2)
val GardenWindowSun = Color(0xFFE8D58C)
val ShopDisplayBase = Color(0xFFC69B69)
val EveningThemePreview = EveningGardenBackdrop
val DarkThemeSurface = Color(0xFF30483A)
val DarkThemeSurfaceVariant = Color(0xFF3B604D)
val DarkThemeOutline = Color(0xFF68806D)
val DarkThemeOutlineVariant = Color(0xFF496454)

data class FlowerPalette(
    val stem: Color,
    val leaf: Color,
    val leafVein: Color,
    val pot: Color,
    val potRim: Color,
    val potShadow: Color,
    val witheredStem: Color,
    val witheredLeaf: Color,
    val seed: Color
)

data class CactusPalette(
    val body: Color,
    val outline: Color,
    val highlight: Color
)

data class BlossomPalette(
    val blossomLight: Color,
    val blossom: Color,
    val blossomDark: Color,
    val center: Color
)

data class LavenderPalette(
    val lavenderLight: Color,
    val lavenderDark: Color
)

object FlowerPalettes {
    val Monstera = FlowerPalette(
        stem = Color(0xFF41694A),
        leaf = Color(0xFF4F7954),
        leafVein = Color(0xFFDCE8D9),
        pot = Color(0xFFBF8744),
        potRim = Color(0xFFE7BD72),
        potShadow = Color(0xFF6F4D35),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )
    val Fern = FlowerPalette(
        stem = Color(0xFF41694A),
        leaf = Color(0xFF688C5D),
        leafVein = Color(0xFFDCE8D9),
        potShadow = Color(0xFF6F4D35),
        pot = Color(0xFF587969),
        potRim = Color(0xFF8AAA98),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )
    val Sprout = FlowerPalette(
        stem = Color(0xFF718261),
        leaf = Color(0xFF7C9069),
        leafVein = Color(0xFFDCE8D9),
        pot = Color(0xFFB8734F),
        potRim = Color(0xFFD89162),
        potShadow = Color(0xFF6F4D35),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )
    val CoralBlossom = FlowerPalette(
        stem = Color(0xFF4E774F),
        leaf = Color(0xFF4F7954),
        leafVein = Color(0xFFDCE8D9),
        pot = Color(0xFFB8734F),
        potRim = Color(0xFFD89162),
        potShadow = Color(0xFF6F4D35),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )
    val LavenderPlant = FlowerPalette(
        stem = Color(0xFF527150),
        leaf = Color(0xFF668060),
        leafVein = Color(0xFFDCE8D9),
        pot = Color(0xFFB8734F),
        potRim = Color(0xFFD89162),
        potShadow = Color(0xFF6F4D35),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )
    val Cactus = FlowerPalette(
        stem = Color(0xFF396E51),
        leaf = Color(0xFF5D956A),
        leafVein = Color(0xFFDCE8D9),
        pot = Color(0xFFB8734F),
        potRim = Color(0xFFD89162),
        potShadow = Color(0xFF6F4D35),
        witheredStem = Color(0xFF88785B),
        witheredLeaf = Color(0xFF9A8059),
        seed = Color(0xFF8C704B)
    )

    val CactusDetails = CactusPalette(
        body = Color(0xFF4F8A61),
        outline = Color(0xFF396E51),
        highlight = Color(0xFF68A073)
    )
    val BlossomDetails = BlossomPalette(
        blossomLight = Color(0xFFF0B29B),
        blossom = Color(0xFFE4A08B),
        blossomDark = Color(0xFFD88975),
        center = Color(0xFFE8B657)
    )
    val LavenderDetails = LavenderPalette(
        lavenderLight = Color(0xFFA189B3),
        lavenderDark = Color(0xFF8F78A4)
    )

    fun forPlant(type: String): FlowerPalette = when (type) {
        "fern" -> Fern
        "sprout" -> Sprout
        "bloom" -> CoralBlossom
        "lavender" -> LavenderPlant
        "cactus" -> Cactus
        else -> Monstera
    }
}
