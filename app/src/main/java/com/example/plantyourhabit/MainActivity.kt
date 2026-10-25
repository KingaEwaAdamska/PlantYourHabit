package com.example.plantyourhabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.res.painterResource
import com.example.plantyourhabit.ui.theme.PlantYourHabitTheme
import com.example.plantyourhabit.ui.theme.Green300
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantYourHabitTheme {
                PlantYourHabitApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PlantYourHabitApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.GARDEN) }
    var gold by rememberSaveable { mutableStateOf(120) }
    var completedHabits by remember { mutableStateOf(setOf(1)) }
    var completedQuests by remember { mutableStateOf(setOf<Int>()) }
    val ownedTrinkets = remember { mutableStateListOf<Int>() }
    val ownedPlants = remember { mutableStateListOf<String>() }
    val ownedBackgrounds = remember { mutableStateListOf("classic") }
    var activeBackground by rememberSaveable { mutableStateOf("classic") }
    val colorScheme = MaterialTheme.colorScheme
    val navigationItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedTextColor = colorScheme.primary,
            unselectedTextColor = colorScheme.primary
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedTextColor = colorScheme.primary
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.Transparent,
            selectedTextColor = colorScheme.primary
        )
    )

    NavigationSuiteScaffold(
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = colorScheme.surface,
            shortNavigationBarContentColor = colorScheme.onSurface,
            navigationBarContainerColor = colorScheme.surface,
            navigationBarContentColor = colorScheme.onSurface,
            navigationRailContainerColor = colorScheme.surface,
            navigationRailContentColor = colorScheme.onSurface,
            navigationDrawerContainerColor = colorScheme.surface,
            navigationDrawerContentColor = colorScheme.onSurface
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        DestinationNavigationIcon(it, it == currentDestination)
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    colors = navigationItemColors
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppHeader(destination = currentDestination, gold = gold)
                when (currentDestination) {
                    AppDestinations.GARDEN -> GardenScreen(
                        completedHabits = completedHabits,
                        activeBackground = activeBackground,
                        onToggleHabit = { id, reward ->
                            val done = completedHabits.contains(id)
                            completedHabits = if (done) completedHabits - id else completedHabits + id
                            gold = (gold + if (done) -reward else reward).coerceAtLeast(0)
                        },
                        onOpenShop = { currentDestination = AppDestinations.SHOP },
                        modifier = Modifier.weight(1f)
                    )
                    AppDestinations.QUESTS -> QuestsScreen(
                        completedQuests = completedQuests,
                        onCompleteQuest = { id, reward ->
                            if (!completedQuests.contains(id)) {
                                completedQuests = completedQuests + id
                                gold += reward
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AppDestinations.SHOP -> ShopScreen(
                        gold = gold,
                        ownedTrinkets = ownedTrinkets,
                        ownedPlants = ownedPlants,
                        ownedBackgrounds = ownedBackgrounds,
                        activeBackground = activeBackground,
                        onBuyTrinket = { id, price ->
                            if (id !in ownedTrinkets && gold >= price) {
                                gold -= price
                                ownedTrinkets.add(id)
                            }
                        },
                        onBuyPlant = { id, price ->
                            if (id !in ownedPlants && gold >= price) {
                                gold -= price
                                ownedPlants.add(id)
                            }
                        },
                        onSelectBackground = { id, price ->
                            if (id in ownedBackgrounds) {
                                activeBackground = id
                            } else if (gold >= price) {
                                gold -= price
                                ownedBackgrounds.add(id)
                                activeBackground = id
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    GARDEN("Garden", R.drawable.ic_garden),
    QUESTS("Quests", R.drawable.ic_quest),
    SHOP("Shop", R.drawable.ic_shop),
}

@Composable
private fun DestinationNavigationIcon(destination: AppDestinations, selected: Boolean) {
    val color by animateColorAsState(
        targetValue = if (selected) Green300 else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 250),
        label = "navigationSelection"
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(destination.icon),
            contentDescription = destination.label,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}