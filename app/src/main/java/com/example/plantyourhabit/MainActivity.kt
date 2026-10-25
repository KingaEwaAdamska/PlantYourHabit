package com.example.plantyourhabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.plantyourhabit.ui.theme.Green300
import com.example.plantyourhabit.ui.theme.Green500
import com.example.plantyourhabit.ui.theme.PlantYourHabitTheme
import java.time.LocalDate

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
    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.GARDEN)
    }
    var gold by rememberSaveable { mutableStateOf(STARTING_GOLD_BALANCE) }
    val habits = remember {
        mutableStateListOf<Habit>().apply {
            addAll(initialHabits)
        }
    }
    var completedQuests by remember { mutableStateOf(setOf<Int>()) }

    val ownedTrinkets = remember { mutableStateListOf<Int>() }
    val ownedPlants = remember { mutableStateListOf<String>() }
    val selectedPlants = remember {
        mutableStateMapOf(1 to "monstera", 2 to "fern", 3 to "sprout")
    }
    val ownedBackgrounds = remember {
        mutableStateListOf("classic")
    }

    var activeBackground by rememberSaveable {
        mutableStateOf("classic")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CustomNavigationBar(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    currentDestination = destination
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppHeader(
                destination = currentDestination,
                gold = gold
            )

            when (currentDestination) {
                AppDestinations.GARDEN -> GardenScreen(
                    habits = habits,
                    ownedTrinkets = ownedTrinkets,
                    activeBackground = activeBackground,
                    selectedPlants = selectedPlants,
                    availablePlants = listOf("monstera", "fern", "sprout") + ownedPlants,
                    onSelectPlant = { habitId, plant ->
                        selectedPlants[habitId] = plant
                    },
                    onToggleHabit = { habit ->
                        val today = LocalDate.now()
                        val done = today.toString() in habit.careDays
                        val updatedHabit = if (done) {
                            habit.removeToday(today)
                        } else {
                            habit.addToday(today)
                        }
                        val habitIndex = habits.indexOfFirst { it.id == habit.id }
                        if (habitIndex >= 0) {
                            habits[habitIndex] = updatedHabit
                        }

                        gold = (
                                gold + if (done) -habit.reward else habit.reward
                                ).coerceAtLeast(0)
                    },
                    onOpenShop = {
                        currentDestination = AppDestinations.SHOP
                    },
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

@Composable
private fun CustomNavigationBar(
    currentDestination: AppDestinations,
    onDestinationSelected: (AppDestinations) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(vertical = AppTokens.StandardInset),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppDestinations.entries.forEach { destination ->
            val selected = destination == currentDestination

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            androidx.compose.foundation.interaction
                                .MutableInteractionSource()
                        }
                    ) {
                        onDestinationSelected(destination)
                    }
                    .padding(vertical = AppTokens.CompactInset),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DestinationNavigationIcon(
                    destination = destination,
                    selected = selected
                )

                Text(
                    text = destination.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
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
private fun DestinationNavigationIcon(
    destination: AppDestinations,
    selected: Boolean
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    val color by animateColorAsState(
        targetValue = if (selected) Green300 else Color.Transparent,
        animationSpec = if (selected) {
            keyframes {
                durationMillis = NAV_SELECTION_ANIMATION_MILLIS

                surfaceColor at 0
                Green500 at NAV_SELECTION_MIDPOINT_MILLIS
                Green300 at NAV_SELECTION_ANIMATION_MILLIS
            }
        } else {
            tween(durationMillis = NAV_SELECTION_ANIMATION_MILLIS)
        },
        label = "navigationSelection"
    )

    Box(
        modifier = Modifier
            .size(AppTokens.NavigationIconSize)
            .clip(RoundedCornerShape(AppTokens.NavigationIconCornerRadius))
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