package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantyourhabit.ui.theme.BeigeBackground
import com.example.plantyourhabit.ui.theme.BeigeMedium
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Gold600
import com.example.plantyourhabit.ui.theme.Gold700
import com.example.plantyourhabit.ui.theme.Green100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.Green500
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green800
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextMuted
import com.example.plantyourhabit.ui.theme.TextSecondary
import com.example.plantyourhabit.ui.theme.Terracotta100
import com.example.plantyourhabit.ui.theme.Terracotta600

private data class ShopProduct(val id: Int, val name: String, val price: Int, val art: String)

private val trinkets = listOf(
    ShopProduct(1, "Tiny mushroom", 40, "🍄"),
    ShopProduct(2, "Lucky snail", 65, "🐌"),
    ShopProduct(3, "Golden star", 90, "✦")
)

private val plantProducts = listOf(
    ShopProduct(1, "Coral blossom", 70, "🌺"),
    ShopProduct(2, "Desert cactus", 85, "🌵"),
    ShopProduct(3, "Soft lavender", 105, "💐")
)

private val gardenThemes = listOf(
    ShopProduct(1, "Classic garden", 0, "🌿"),
    ShopProduct(2, "Warm sunroom", 90, "☀️"),
    ShopProduct(3, "Quiet evening", 120, "🌙"),
    ShopProduct(4, "Forest nook", 150, "🌲")
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CreamBackground)
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        Card(
            shape = RoundedCornerShape(27.dp),
            colors = CardDefaults.cardColors(containerColor = BeigeMedium)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("✦", modifier = Modifier.align(Alignment.TopStart).padding(28.dp), color = Color.White, fontSize = 24.sp)
                Text("✦", modifier = Modifier.align(Alignment.TopEnd).padding(24.dp), color = Color.White, fontSize = 16.sp)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 21.dp)
                        .background(Color(0xFFC69B69), CircleShape)
                        .padding(horizontal = 27.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text("🍄", fontSize = 42.sp)
                    Spacer(Modifier.width(20.dp))
                    Text("🐌", fontSize = 40.sp)
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionKicker("MAKE IT YOURS", Green700)
            Text(
                "Find something lovely",
                color = Green900,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Spend the gold you earn on tiny treasures for your garden shelf.",
                modifier = Modifier.padding(top = 7.dp),
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
        ShopSectionHeading("FOR YOUR SHELF", "Tiny trinkets", "${ownedTrinkets.size}/${trinkets.size}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        Text(
            "✦  New treasures arrive every Sunday",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            color = TextMuted,
            fontSize = 10.sp
        )
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
        shape = RoundedCornerShape(19.dp),
        color = if (owned) Green100 else CreamSurface
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .background(BeigeBackground, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(product.art, fontSize = 36.sp)
            }
            Text(
                product.name,
                modifier = Modifier.padding(top = 8.dp),
                color = Green900,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Text(
                if (owned) "On shelf ✓" else "🪙 ${product.price}",
                modifier = Modifier.padding(top = 4.dp),
                color = if (owned) Green700 else Gold700,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun ShopListRow(
    art: String,
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
        shape = RoundedCornerShape(19.dp),
        color = if (owned) Green100 else CreamSurface
    ) {
        Row(
            modifier = Modifier.padding(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(66.dp)
                    .background(Green100, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(art, fontSize = 32.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(eyebrow, color = Gold700, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Text(name, color = Green900, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(detail, color = TextMuted, fontSize = 9.sp)
            }
            Text(
                if (owned) "✓ Owned" else "🪙 $price",
                modifier = Modifier
                    .background(if (owned) Green200 else Gold100, CircleShape)
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                color = if (owned) Green700 else Gold700,
                fontSize = 9.sp,
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
        3 -> Color(0xFF8992A0)
        else -> Green500
    }
    Surface(
        modifier = modifier.clickable(enabled = owned || canAfford, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (isActive) Green100 else CreamSurface,
        shadowElevation = if (isActive) 2.dp else 0.dp
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .background(previewColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(product.art, fontSize = 25.sp)
                if (isActive) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(5.dp),
                        shape = CircleShape,
                        color = Green700
                    ) {
                        Text("✓", modifier = Modifier.padding(4.dp), color = Color.White, fontSize = 10.sp)
                    }
                }
            }
            Text(
                product.name,
                modifier = Modifier.padding(start = 2.dp, top = 6.dp),
                color = Green900,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
            Text(
                when {
                    isActive -> "Using now"
                    owned -> "Tap to use"
                    else -> "🪙 ${product.price}"
                },
                modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                color = if (owned) Green700 else Gold700,
                fontSize = 8.sp,
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
            .padding(start = 3.dp, top = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            SectionKicker(kicker, TextSecondary)
            Text(
                title,
                color = Green900,
                fontFamily = FontFamily.Serif,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(count, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

