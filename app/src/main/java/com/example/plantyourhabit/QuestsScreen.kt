package com.example.plantyourhabit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.plantyourhabit.ui.theme.BeigeBackground
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Gold600
import com.example.plantyourhabit.ui.theme.Gold700
import com.example.plantyourhabit.ui.theme.Green100
import com.example.plantyourhabit.ui.theme.Green200
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green800
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextSecondary

data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<String>,
    val difficulty: String,
    val reward: Int
)

val initialQuests = listOf(
    Quest(1, "Book dentist appointment", "Find a nearby clinic and choose a time that works.", listOf("Life admin", "Health"), "Medium", 35),
    Quest(2, "Organize the photo drawer", "Sort loose prints into keep, share, and archive piles.", listOf("Home"), "Medium", 25),
    Quest(3, "Try a new soup recipe", "Pick something seasonal and make enough for tomorrow.", listOf("Someday", "Cooking"), "Easy", 20),
    Quest(4, "Send the postcards", "Write a quick note and drop them in the mailbox.", listOf("Friends"), "Easy", 15)
)

@Composable
fun QuestsScreen(
    quests: List<Quest>,
    completedQuests: Set<Int>,
    onCompleteQuest: (Int, Int) -> Unit,
    onCreateQuest: (Quest) -> Unit,
    onRemoveQuest: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateQuestDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CreamBackground)
            .padding(horizontal = QuestsTokens.PageInset, vertical = QuestsTokens.MicroPadding)
    ) {
        Card(
            shape = RoundedCornerShape(QuestsTokens.LargeCardRadius),
            colors = CardDefaults.cardColors(containerColor = Green200)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(QuestsTokens.LargeSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionKicker("BACKLOG", Green700)
                    Text(
                        "Small adventures,\nwhenever you're ready.",
                        color = Green900,
                        fontFamily = FontFamily.Serif,
                        fontSize = QuestsTokens.QuestHeroText,
                        lineHeight = QuestsTokens.QuestHeroLineHeight,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(QuestsTokens.ContentInset))
                    Text(
                        "No deadlines here. Pick a sidequest and earn a little gold.",
                        color = TextSecondary,
                        fontSize = QuestsTokens.BodyText,
                        lineHeight = QuestsTokens.SupportingTitleText
                    )
                }
                Image(
                    painter = painterResource(R.drawable.ic_quest_map),
                    contentDescription = "Quest map",
                    modifier = Modifier.size(QuestsTokens.QuestMapText.value.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(top = QuestsTokens.SectionInset),
            verticalArrangement = Arrangement.spacedBy(QuestsTokens.ComfortableInset)
        ) {
            quests.forEach { quest ->
                key(quest.id) {
                    val done = quest.id in completedQuests
                    val visibleState = remember { MutableTransitionState(true) }
                    val removeQuest by rememberUpdatedState(onRemoveQuest)

                    LaunchedEffect(done) {
                        if (done) visibleState.targetState = false
                    }
                    LaunchedEffect(visibleState.isIdle, visibleState.currentState) {
                        if (done && visibleState.isIdle && !visibleState.currentState) {
                            removeQuest(quest.id)
                        }
                    }

                    AnimatedVisibility(
                        visibleState = visibleState,
                        exit = fadeOut(animationSpec = tween(700)) +
                            shrinkVertically(
                                animationSpec = tween(700),
                                shrinkTowards = Alignment.Top
                            )
                    ) {
                        QuestCard(quest = quest, done = done) {
                            onCompleteQuest(quest.id, quest.reward)
                        }
                    }
                }
            }
        }
        Button(
            onClick = { showCreateQuestDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = QuestsTokens.PageInset),
            shape = RoundedCornerShape(QuestsTokens.AddQuestButtonCornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Green800)
        ) {
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(QuestsTokens.CheckmarkSize)
                )
                Text("Add a sidequest", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(QuestsTokens.LargeInset))
    }

    if (showCreateQuestDialog) {
        CreateQuestDialog(
            onDismiss = { showCreateQuestDialog = false },
            onCreate = { title, description, tags, difficulty, reward ->
                val nextId = (quests.maxOfOrNull(Quest::id) ?: 0) + 1
                onCreateQuest(Quest(nextId, title, description, tags, difficulty, reward))
                showCreateQuestDialog = false
            }
        )
    }
}

@Composable
private fun CreateQuestDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, List<String>, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Easy") }
    var rewardText by remember { mutableStateOf("20") }
    val reward = rewardText.toIntOrNull()
    val canCreate = title.isNotBlank() && description.isNotBlank() && reward != null && reward > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create a sidequest") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(QuestsTokens.StandardInset)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Quest title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    singleLine = true
                )
                Text("Difficulty", color = Green900, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(QuestsTokens.CompactInset)) {
                    listOf("Easy", "Medium", "Hard").forEach { option ->
                        TagChip(
                            text = if (difficulty == option) "✓ $option" else option,
                            color = if (difficulty == option) Green200 else BeigeBackground,
                            modifier = Modifier.clickable { difficulty = option }
                        )
                    }
                }
                OutlinedTextField(
                    value = rewardText,
                    onValueChange = { rewardText = it.filter(Char::isDigit) },
                    label = { Text("Gold reward") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validReward = reward ?: return@Button
                    onCreate(
                        title.trim(),
                        description.trim(),
                        tags.split(",").map(String::trim).filter(String::isNotEmpty),
                        difficulty,
                        validReward
                    )
                },
                enabled = canCreate
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun QuestCard(quest: Quest, done: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !done, onClick = onClick),
        shape = RoundedCornerShape(QuestsTokens.QuestCardCornerRadius),
        color = if (done) Green100 else CreamSurface,
        shadowElevation = QuestsTokens.Hairline
    ) {
        Row(
            modifier = Modifier.padding(horizontal = QuestsTokens.CardContentInset, vertical = QuestsTokens.CardInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (done) Green700 else Color.Transparent
            ) {
                if (done) {
                    Image(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Completed",
                        modifier = Modifier
                            .size(QuestsTokens.CheckmarkText.value.dp)
                            .padding(top = QuestsTokens.TinyInset)
                    )
                }
            }
            Spacer(Modifier.width(QuestsTokens.ContentInset))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(QuestsTokens.CompactInset)) {
                Row(horizontalArrangement = Arrangement.spacedBy(QuestsTokens.CompactInset)) {
                    TagChip(quest.difficulty, if (quest.difficulty == "Easy") Green100 else Gold100)
                    quest.tags.forEach { TagChip(it, BeigeBackground) }
                }
                Text(
                    quest.title,
                    color = Green900,
                    fontWeight = FontWeight.Bold,
                    fontSize = QuestsTokens.CardTitleText,
                    textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(quest.description, color = TextSecondary, fontSize = QuestsTokens.SupportingText, lineHeight = QuestsTokens.CardTitleText)
            }
            Spacer(Modifier.width(QuestsTokens.StandardInset))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.ic_gold_coin),
                    contentDescription = "Gold reward",
                    modifier = Modifier.height(QuestsTokens.CardTitleText.value.dp)
                )
                Text(
                    if (done) "Done" else quest.reward.toString(),
                    color = Gold700,
                    fontSize = QuestsTokens.SupportingText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TagChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = CircleShape, color = color) {
        Text(
            text.uppercase(),
            modifier = Modifier.padding(horizontal = QuestsTokens.SmallInset, vertical = QuestsTokens.TightInset),
            color = Green700,
            fontSize = QuestsTokens.SmallLabelText,
            fontWeight = FontWeight.Bold
        )
    }
}
