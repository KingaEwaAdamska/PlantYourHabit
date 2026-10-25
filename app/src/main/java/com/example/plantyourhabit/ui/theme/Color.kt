package com.example.plantyourhabit.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

// Primary — naturalna, leśna zieleń
private val LightGreen900 = Color(0xFF26392F)
private val LightGreen800 = Color(0xFF355947)
private val LightGreen700 = Color(0xFF496454)
val Green600 = Color(0xFF527560)
val Green500 = Color(0xFF688C5D)
val Green300 = Color(0xFFAEC7B1)
private val LightGreen200 = Color(0xFFDCE8D9)
private val LightGreen100 = Color(0xFFEDF2E8)

// Background — ciepłe, naturalne tło
private val LightCreamBackground = Color(0xFFF8F5ED)
private val LightCreamSurface = Color(0xFFFFFDF8)
private val LightBeigeBackground = Color(0xFFE7E1D3)
private val LightBeigeLight = Color(0xFFF6F1E5)
private val LightBeigeMedium = Color(0xFFE8D8B5)

// Accent — złoto i miód
private val LightGold700 = Color(0xFF8D6424)
val Gold600 = Color(0xFF99722D)
val Gold400 = Color(0xFFEAB866)
val Gold300 = Color(0xFFF6D57C)
private val LightGold100 = Color(0xFFF7E6B4)

// Terracotta — doniczki i dekoracje
val Terracotta700 = Color(0xFFA75D45)
val Terracotta600 = Color(0xFFB8634D)
val Terracotta500 = Color(0xFFC47F57)
val Terracotta300 = Color(0xFFD89162)
val Terracotta100 = Color(0xFFF3DDD5)

// Text — teksty i elementy pomocnicze
val TextPrimary = Color(0xFF26392F)
private val LightTextSecondary = Color(0xFF66766B)
private val LightTextMuted = Color(0xFF909A91)
val BorderColor = Color(0xFFE3E2D9)
val DividerColor = Color(0xFFE7E5DD)

// Flowers — dodatkowe akcenty
val FlowerPink = Color(0xFFD88975)
val Lavender = Color(0xFF9A81AE)
val LeafGreen = Color(0xFF7C9069)
val EveningGardenBackdrop = Color(0xFFD4C5D0)
val EveningGardenSunset = Color(0xFFE6A16F)
val ForestGardenBackdrop = Color(0xFFB7C6A8)
val SunroomGardenBackdrop = Color(0xFFE8D8B5)
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
val DarkThemeSurface = Color(0xFF2B2E30)
val DarkThemeSurfaceVariant = Color(0xFF3A3E40)
val DarkThemeOutline = Color(0xFF777D7D)
val DarkThemeOutlineVariant = Color(0xFF565C5D)

val LocalAppDarkTheme = staticCompositionLocalOf { false }

private val DarkBackground = Color(0xFF202224)
private val DarkSurface = Color(0xFF2B2E30)
private val DarkSurfaceElevated = Color(0xFF393D3F)
private val DarkText = Color(0xFFE9E9E5)
private val DarkTextSecondary = Color(0xFFC1C4C1)
private val DarkTextMuted = Color(0xFFA3A7A5)
private val DarkGold = Color(0xFFF0C777)
private val DarkGoldSurface = Color(0xFF51442A)
private val DarkBorder = Color(0xFF555B59)
private val DarkBeigeSurface = Color(0xFF414342)

@get:Composable
val Green900: Color
    get() = if (LocalAppDarkTheme.current) DarkText else LightGreen900

@get:Composable
val Green800: Color
    get() = if (LocalAppDarkTheme.current) DarkSurfaceElevated else LightGreen800

@get:Composable
val Green700: Color
    get() = if (LocalAppDarkTheme.current) Color(0xFFB9D2BC) else LightGreen700

@get:Composable
val Green200: Color
    get() = if (LocalAppDarkTheme.current) Color(0xFF454B48) else LightGreen200

@get:Composable
val Green100: Color
    get() = if (LocalAppDarkTheme.current) Color(0xFF353A38) else LightGreen100

@get:Composable
val CreamBackground: Color
    get() = if (LocalAppDarkTheme.current) DarkBackground else LightCreamBackground

@get:Composable
val CreamSurface: Color
    get() = if (LocalAppDarkTheme.current) DarkSurface else LightCreamSurface

@get:Composable
val BeigeBackground: Color
    get() = if (LocalAppDarkTheme.current) DarkSurfaceElevated else LightBeigeBackground

@get:Composable
val BeigeLight: Color
    get() = if (LocalAppDarkTheme.current) DarkSurface else LightBeigeLight

@get:Composable
val BeigeMedium: Color
    get() = if (LocalAppDarkTheme.current) DarkBeigeSurface else LightBeigeMedium

@get:Composable
val Gold700: Color
    get() = if (LocalAppDarkTheme.current) DarkGold else LightGold700

@get:Composable
val Gold100: Color
    get() = if (LocalAppDarkTheme.current) DarkGoldSurface else LightGold100

@get:Composable
val TextSecondary: Color
    get() = if (LocalAppDarkTheme.current) DarkTextSecondary else LightTextSecondary

@get:Composable
val TextMuted: Color
    get() = if (LocalAppDarkTheme.current) DarkTextMuted else LightTextMuted

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
