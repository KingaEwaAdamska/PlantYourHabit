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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Gold700
import com.example.plantyourhabit.ui.theme.Green100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.ForestGardenBackdrop
import com.example.plantyourhabit.ui.theme.EveningGardenBackdrop
import com.example.plantyourhabit.ui.theme.EveningGardenSunset
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextMuted
import com.example.plantyourhabit.ui.theme.TextSecondary
import com.example.plantyourhabit.ui.theme.ShopDisplayBase

private enum class ShopArt(val drawable: Int) {
    MUSHROOM(R.drawable.ic_shop_mushroom),
    SNAIL(R.drawable.ic_shop_snail),
    LANTERN(R.drawable.ic_shop_lantern),
    BLOSSOM(R.drawable.ic_shop_blossom),
    CACTUS(R.drawable.ic_shop_cactus),
    LAVENDER(R.drawable.ic_shop_lavender),
    LEAF(R.drawable.ic_shop_leaf),
    SUN(R.drawable.ic_shop_sun),
    MOON(R.drawable.ic_shop_moon),
    TREE(R.drawable.ic_shop_tree)
}

private data class ShopProduct(val id: Int, val name: String, val price: Int, val art: ShopArt)

@Composable
private fun ShopArtwork(art: ShopArt, modifier: Modifier = Modifier) {
    Image(painterResource(art.drawable), contentDescription = null, modifier = modifier)
}

private val trinkets = listOf(
    ShopProduct(1, "Tiny mushroom", 40, ShopArt.MUSHROOM),
    ShopProduct(2, "Lucky snail", 65, ShopArt.SNAIL),
    ShopProduct(3, "Golden lantern", 90, ShopArt.LANTERN)
)

private val plantProducts = listOf(
    ShopProduct(1, "Coral blossom", 70, ShopArt.BLOSSOM),
    ShopProduct(2, "Desert cactus", 85, ShopArt.CACTUS),
    ShopProduct(3, "Soft lavender", 105, ShopArt.LAVENDER)
)

private val gardenThemes = listOf(
    ShopProduct(1, "Classic garden", 0, ShopArt.LEAF),
    ShopProduct(2, "Warm sunroom", 90, ShopArt.SUN),
    ShopProduct(3, "Quiet evening", 120, ShopArt.MOON),
    ShopProduct(4, "Forest nook", 150, ShopArt.TREE)
)

private fun backgroundKey(id: Int): String = when (id) {
    1 -> "classic"
    2 -> "sunroom"
    3 -> "evening"
    else -> "forest"
}

@Composable
fun ShopScreen(
    gold: Int,
    ownedTrinkets: List<Int>,
    ownedPlants: List<String>,
    ownedBackgrounds: List<String>,
    activeBackground: String,
    onBuyTrinket: (Int, Int) -> Unit,
    onBuyPlant: (String, Int) -> Unit,
    onSelectBackground: (String, Int) -> Unit,
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
        Card(
            shape = RoundedCornerShape(ShopTokens.LargeCardRadius),
            colors = CardDefaults.cardColors(containerColor = BeigeMedium)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ShopTokens.ShopIllustrationHeight),
                contentAlignment = Alignment.Center
            ) {
                ShopArtwork(ShopArt.LANTERN, Modifier.align(Alignment.TopStart).padding(ShopTokens.ShopStarInnerInset).size(ShopTokens.QuestHeroText.value.dp))
                ShopArtwork(ShopArt.LANTERN, Modifier.align(Alignment.TopEnd).padding(ShopTokens.ShopStarOuterInset).size(ShopTokens.NavigationTitleText.value.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = ShopTokens.ProductDecorationInset)
                        .background(ShopDisplayBase, CircleShape)
                        .padding(horizontal = ShopTokens.ShopArtworkHorizontalPadding, vertical = ShopTokens.ContentInset),
                    verticalAlignment = Alignment.Bottom
                ) {
                    ShopArtwork(ShopArt.MUSHROOM, Modifier.size(ShopTokens.ShopPlantPreviewSize))
                    Spacer(Modifier.width(ShopTokens.LargeSpacing))
                    ShopArtwork(ShopArt.SNAIL, Modifier.size(ShopTokens.ShopPlantPreviewSize))
                }
            }
        }
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
                "Spend the gold you earn on tiny treasures for your garden shelf.",
                modifier = Modifier.padding(top = ShopTokens.LabelInset),
                color = TextSecondary,
                fontSize = ShopTokens.BodyText,
                lineHeight = ShopTokens.SupportingTitleText
            )
        }
        ShopSectionHeading("FOR YOUR SHELF", "Tiny trinkets", "${ownedTrinkets.size}/${trinkets.size}")
        Row(horizontalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)) {
            trinkets.forEach { product ->
                val owned = product.id in ownedTrinkets
                val canAfford = gold >= product.price
                ProductCard(
                    product = product,
                    owned = owned,
                    canAfford = canAfford,
                    modifier = Modifier.weight(1f),
                    onClick = { onBuyTrinket(product.id, product.price) }
                )
            }
        }
        ShopSectionHeading("NEW VARIETIES", "Plants to grow", "${ownedPlants.size}/${plantProducts.size}")
        Column(verticalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)) {
            plantProducts.forEach { product ->
                val id = when (product.id) {
                    1 -> "bloom"
                    2 -> "cactus"
                    else -> "lavender"
                }
                val owned = id in ownedPlants
                ShopListRow(
                    art = product.art,
                    eyebrow = if (owned) "IN YOUR COLLECTION" else "PLANT VARIETY",
                    name = product.name,
                    detail = "Use this style for any habit",
                    price = product.price,
                    owned = owned,
                    canAfford = gold >= product.price,
                    onClick = { onBuyPlant(id, product.price) }
                )
            }
        }
        ShopSectionHeading(
            "SET THE MOOD",
            "Garden backgrounds",
            "${ownedBackgrounds.size}/${gardenThemes.size}"
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)
        ) {
            gardenThemes.take(2).forEach { theme ->
                BackgroundCard(
                    product = theme,
                    activeBackground = activeBackground,
                    owned = backgroundKey(theme.id) in ownedBackgrounds,
                    canAfford = gold >= theme.price,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectBackground(backgroundKey(theme.id), theme.price) }
                )
            }
        }
        Spacer(Modifier.height(ShopTokens.StandardInset))
        Row(horizontalArrangement = Arrangement.spacedBy(ShopTokens.StandardInset)) {
            gardenThemes.drop(2).forEach { theme ->
                BackgroundCard(
                    product = theme,
                    activeBackground = activeBackground,
                    owned = backgroundKey(theme.id) in ownedBackgrounds,
                    canAfford = gold >= theme.price,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectBackground(backgroundKey(theme.id), theme.price) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ShopTokens.SectionInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShopArtwork(ShopArt.LANTERN, Modifier.size(ShopTokens.SupportingText.value.dp))
            Text(
                "  New treasures arrive every Sunday",
                color = TextMuted,
                fontSize = ShopTokens.SupportingText
            )
        }
    }
}
}
}

@Composable
private fun ProductCard(
    product: ShopProduct,
    owned: Boolean,
    canAfford: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(enabled = !owned && canAfford, onClick = onClick),
        shape = RoundedCornerShape(ShopTokens.ShopCardRadius),
        color = if (owned) Green100 else CreamSurface
    ) {
        Column(
            modifier = Modifier.padding(ShopTokens.StandardInset),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ShopTokens.ShopProductPreviewHeight)
                    .background(BeigeBackground, RoundedCornerShape(ShopTokens.ShopProductPreviewCornerRadius)),
                contentAlignment = Alignment.Center
            ) {
                ShopArtwork(product.art, Modifier.fillMaxSize().padding(ShopTokens.SmallInset))
            }
            Text(
                product.name,
                modifier = Modifier.padding(top = ShopTokens.StandardInset),
                color = Green900,
                fontWeight = FontWeight.Bold,
                fontSize = ShopTokens.SupportingText
            )
            Text(
                if (owned) "On shelf" else "Gold · ${product.price}",
                modifier = Modifier.padding(top = ShopTokens.CompactInset),
                color = if (owned) Green700 else Gold700,
                fontWeight = FontWeight.Bold,
                fontSize = ShopTokens.CaptionText
            )
        }
    }
}

@Composable
private fun ShopListRow(
    art: ShopArt,
    eyebrow: String,
    name: String,
    detail: String,
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
        color = if (owned) Green100 else CreamSurface
    ) {
        Row(
            modifier = Modifier.padding(ShopTokens.ComfortableInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(ShopTokens.ShopPlantPreviewSize)
                    .background(Green100, RoundedCornerShape(ShopTokens.ShopPlantPreviewCornerRadius)),
                contentAlignment = Alignment.Center
            ) {
                ShopArtwork(art, Modifier.fillMaxSize().padding(ShopTokens.SmallInset))
            }
            Spacer(Modifier.width(ShopTokens.ContentInset))
            Column(modifier = Modifier.weight(1f)) {
                Text(eyebrow, color = Gold700, fontSize = ShopTokens.SmallLabelText, fontWeight = FontWeight.Bold)
                Text(name, color = Green900, fontSize = ShopTokens.BodyText, fontWeight = FontWeight.Bold)
                Text(detail, color = TextMuted, fontSize = ShopTokens.CaptionText)
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
    product: ShopProduct,
    activeBackground: String,
    owned: Boolean,
    canAfford: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isActive = activeBackground == backgroundKey(product.id)
    val previewColor = when (product.id) {
        1 -> Green300
        2 -> BeigeMedium
        3 -> EveningGardenBackdrop
        else -> ForestGardenBackdrop
    }
    Surface(
        modifier = modifier.clickable(enabled = owned || canAfford, onClick = onClick),
        shape = RoundedCornerShape(ShopTokens.ShopBackgroundCardCornerRadius),
        color = if (isActive) Green100 else CreamSurface,
        shadowElevation = if (isActive) ShopTokens.TinyInset else ShopTokens.NoElevation
    ) {
        Column(modifier = Modifier.padding(ShopTokens.SmallInset)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ShopTokens.ShopProductPreviewHeight)
                    .background(
                        brush = if (product.id == 3) {
                            Brush.verticalGradient(listOf(EveningGardenSunset, EveningGardenBackdrop))
                        } else {
                            Brush.verticalGradient(listOf(previewColor, previewColor))
                        },
                        shape = RoundedCornerShape(ShopTokens.CardInset)
                    ),
                contentAlignment = Alignment.Center
            ) {
                ShopArtwork(product.art, Modifier.fillMaxSize().padding(ShopTokens.SmallInset))
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
                product.name,
                modifier = Modifier.padding(start = ShopTokens.TinyInset, top = ShopTokens.SmallInset),
                color = Green900,
                fontWeight = FontWeight.Bold,
                fontSize = ShopTokens.CaptionText
            )
            Text(
                when {
                    isActive -> "Using now"
                    owned -> "Tap to use"
                    else -> "Gold · ${product.price}"
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
