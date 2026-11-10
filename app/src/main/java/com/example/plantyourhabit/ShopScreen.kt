package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.plantyourhabit.ui.theme.BeigeBackground
import com.example.plantyourhabit.ui.theme.BeigeMedium
import com.example.plantyourhabit.ui.theme.SunroomGardenBackdrop
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Gold700
import com.example.plantyourhabit.ui.theme.Green100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextMuted
import com.example.plantyourhabit.ui.theme.TextSecondary
import com.example.plantyourhabit.ui.theme.ShopDisplayBase
import com.example.plantyourhabit.ui.theme.LocalAppDarkTheme

private val DarkShopCardSurface = Color(0xFF373B3D)
private val DarkShopCardOwned = Color(0xFF454B48)
private val DarkShopIconSurface = Color(0xFF747876)

private enum class ShopArt(val drawable: Int) {
    MONEY_PLANT(R.drawable.ic_shop_money_plant),
    CACTUS(R.drawable.ic_shop_cactus),
    ORCHID(R.drawable.ic_shop_orchid)
}

private data class ShopProduct(val id: Int, val name: String, val price: Int, val art: ShopArt)

@Composable
private fun ShopArtwork(art: ShopArt, modifier: Modifier = Modifier) {
    Image(painterResource(art.drawable), contentDescription = null, modifier = modifier)
}

private val plantProducts = listOf(
    ShopProduct(1, "Chinese money plant", 70, ShopArt.MONEY_PLANT),
    ShopProduct(2, "Desert cactus", 85, ShopArt.CACTUS),
    ShopProduct(3, "Orchid", 105, ShopArt.ORCHID)
)

@Composable
fun ShopScreen(
    gold: Int,
    ownedPlants: List<PlantType>,
    ownedThemes: List<GardenTheme>,
    activeTheme: GardenTheme,
    onBuyPlant: (PlantType, Int) -> Unit,
    onSelectTheme: (GardenTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentPadding = PaddingValues(
            horizontal = ShopTokens.PageInset,
            vertical = ShopTokens.MicroPadding
        )
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ShopTokens.LargeSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionKicker("MAKE IT YOURS", Green700)
            Text(
                "Find something lovely",
                color = Green900,
                fontFamily = FontFamily.Serif,
                fontSize = ShopTokens.LargeBodyText,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Spend the gold you earn on new plants and garden backgrounds.",
                modifier = Modifier.padding(top = ShopTokens.LabelInset),
                color = TextSecondary,
                fontSize = ShopTokens.BodyText,
                lineHeight = ShopTokens.SupportingTitleText
            )
        }
        ShopSectionHeading("NEW VARIETIES", "Plants to grow", "${ownedPlants.size}/${plantProducts.size}")
        Column(verticalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)) {
            plantProducts.forEach { product ->
                val plantType = when (product.id) {
                    1 -> PlantType.CHINESE_MONEY_PLANT
                    2 -> PlantType.CACTUS
                    else -> PlantType.ORCHID
                }
                val owned = plantType in ownedPlants
                ShopListRow(
                    art = product.art,
                    eyebrow = if (owned) "IN YOUR COLLECTION" else null,
                    name = product.name,
                    price = product.price,
                    owned = owned,
                    canAfford = gold >= product.price,
                    onClick = { onBuyPlant(plantType, product.price) }
                )
            }
        }
        ShopSectionHeading(
            "SET THE MOOD",
            "Garden backgrounds",
            "${ownedThemes.size}/${GardenTheme.entries.size}"
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)
        ) {
            GardenTheme.entries.take(2).forEach { theme ->
                BackgroundCard(
                    theme = theme,
                    activeTheme = activeTheme,
                    owned = theme in ownedThemes,
                    canAfford = gold >= theme.price,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTheme(theme) }
                )
            }
        }
        Spacer(Modifier.height(ShopTokens.StandardInset))
        Row(horizontalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)) {
            GardenTheme.entries.drop(2).forEach { theme ->
                BackgroundCard(
                    theme = theme,
                    activeTheme = activeTheme,
                    owned = theme in ownedThemes,
                    canAfford = gold >= theme.price,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTheme(theme) }
                )
            }
        }
    }
}
}
}

@Composable
private fun ShopListRow(
    art: ShopArt,
    eyebrow: String?,
    name: String,
    price: Int,
    owned: Boolean,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = owned || canAfford, onClick = onClick),
        shape = RoundedCornerShape(ShopTokens.ShopCardRadius),
        color = if (LocalAppDarkTheme.current) {
            if (owned) DarkShopCardOwned else DarkShopCardSurface
        } else if (owned) Green100 else CreamSurface
    ) {
        Row(
            modifier = Modifier.padding(ShopTokens.ComfortableInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(ShopTokens.ShopPlantPreviewSize)
                    .background(
                        if (LocalAppDarkTheme.current) DarkShopIconSurface else Green100,
                        RoundedCornerShape(ShopTokens.ShopPlantPreviewCornerRadius)
                    ),
                contentAlignment = Alignment.Center
            ) {
                ShopArtwork(art, Modifier.fillMaxSize().padding(ShopTokens.SmallInset))
            }
            Spacer(Modifier.width(ShopTokens.ContentInset))
            Column(modifier = Modifier.weight(1f)) {
                eyebrow?.let {
                    Text(it, color = Gold700, fontSize = ShopTokens.SmallLabelText, fontWeight = FontWeight.Bold)
                }
                Text(name, color = Green900, fontSize = ShopTokens.BodyText, fontWeight = FontWeight.Bold)
            }
            Text(
                if (owned) "Owned" else "Gold · $price",
                modifier = Modifier
                    .background(if (owned) Green200 else Gold100, CircleShape)
                    .padding(horizontal = ShopTokens.StandardInset, vertical = ShopTokens.LabelInset),
                color = if (owned) Green700 else Gold700,
                fontSize = ShopTokens.CaptionText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BackgroundCard(
    theme: GardenTheme,
    activeTheme: GardenTheme,
    owned: Boolean,
    canAfford: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isActive = activeTheme == theme
    Surface(
        modifier = modifier.clickable(enabled = owned || canAfford, onClick = onClick),
        shape = RoundedCornerShape(ShopTokens.ShopBackgroundCardCornerRadius),
        color = if (LocalAppDarkTheme.current) {
            if (isActive) DarkShopCardOwned else DarkShopCardSurface
        } else if (isActive) Green100 else CreamSurface,
        shadowElevation = if (isActive) ShopTokens.TinyInset else ShopTokens.NoElevation
    ) {
        Column(modifier = Modifier.padding(ShopTokens.SmallInset)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ShopTokens.ShopProductPreviewHeight)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(theme.previewTopColor, theme.previewBottomColor)
                        ),
                        shape = RoundedCornerShape(ShopTokens.CardInset)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(theme.shopArtwork),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(ShopTokens.SmallInset)
                )
                if (isActive) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(ShopTokens.MicroPadding),
                        shape = CircleShape,
                        color = Green700
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = "Selected",
                            modifier = Modifier.padding(ShopTokens.CompactInset).size(ShopTokens.SupportingText.value.dp)
                        )
                    }
                }
            }
            Text(
                theme.displayName,
                modifier = Modifier.padding(start = ShopTokens.TinyInset, top = ShopTokens.SmallInset),
                color = Green900,
                fontWeight = FontWeight.Bold,
                fontSize = ShopTokens.CaptionText
            )
            Text(
                when {
                    isActive -> "Using now"
                    owned -> "Tap to use"
                    else -> "Gold · ${theme.price}"
                },
                modifier = Modifier.padding(start = ShopTokens.TinyInset, top = ShopTokens.TinyInset),
                color = if (owned) Green700 else Gold700,
                fontSize = ShopTokens.SmallLabelText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ShopSectionHeading(kicker: String, title: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = ShopTokens.TightInset, top = ShopTokens.LargeSpacing, bottom = ShopTokens.ContentInset),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            SectionKicker(kicker, TextSecondary)
            Text(
                title,
                color = Green900,
                fontFamily = FontFamily.Serif,
                fontSize = ShopTokens.ShopTitleText,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(count, color = TextMuted, fontSize = ShopTokens.SupportingText, fontWeight = FontWeight.Bold)
    }
}
