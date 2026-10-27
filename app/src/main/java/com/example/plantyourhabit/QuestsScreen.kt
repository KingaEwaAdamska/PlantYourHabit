package com.example.plantyourhabit

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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    onUpdateQuest: (Quest) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateQuestDialog by remember { mutableStateOf(false) }
    var questBeingEdited by remember { mutableStateOf<Quest?>(null) }
    var showCompletedQuests by remember { mutableStateOf(true) }
    var selectedTagFilters by remember { mutableStateOf(emptySet<String>()) }
    val availableTags = quests
        .flatMap(Quest::tags)
        .distinctBy { it.trim().lowercase() }
        .sortedBy { it.lowercase() }
    val filteredQuests = (if (selectedTagFilters.isEmpty()) {
        quests
    } else {
        quests.filter { quest ->
            quest.tags.any { it.trim().lowercase() in selectedTagFilters }
        }
    }).filter { showCompletedQuests || it.id !in completedQuests }
        .sortedBy { it.id in completedQuests }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = QuestsTokens.PageInset),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            vertical = QuestsTokens.MicroPadding
        ),
        verticalArrangement = Arrangement.spacedBy(QuestsTokens.ComfortableInset)
    ) {
        item {
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
        }
        if (availableTags.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(QuestsTokens.CompactInset)
                ) {
                    SectionKicker("FILTER BY TAG", Green700)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(QuestsTokens.CompactInset)
                    ) {
                        TagChip(
                            text = "All",
                            color = if (selectedTagFilters.isEmpty()) Green200 else BeigeBackground,
                            modifier = Modifier.clickable { selectedTagFilters = emptySet() }
                        )
                        availableTags.forEach { tag ->
                            val normalizedTag = tag.trim().lowercase()
                            val selected = normalizedTag in selectedTagFilters
                            TagChip(
                                text = tag,
                                color = if (selected) Green200 else BeigeBackground,
                                modifier = Modifier.clickable {
                                    selectedTagFilters = if (selected) {
                                        selectedTagFilters - normalizedTag
                                    } else {
                                        selectedTagFilters + normalizedTag
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        item {
            Column {
                Text(
                    "Tap a quest to edit it, or check it off to complete.",
                    color = TextSecondary,
                    fontSize = QuestsTokens.SupportingText
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showCompletedQuests,
                        onCheckedChange = { showCompletedQuests = it }
                    )
                    Text(
                        "Show completed quests",
                        color = Green900,
                        fontSize = QuestsTokens.SupportingText,
                        modifier = Modifier.clickable {
                            showCompletedQuests = !showCompletedQuests
                        }
                    )
                }
            }
        }
        if (filteredQuests.isEmpty()) {
            item {
                Text(
                    "No quests match these filters.",
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = QuestsTokens.StandardInset)
                )
            }
        }
        items(filteredQuests, key = Quest::id) { quest ->
            QuestCard(
                modifier = Modifier.animateItem(),
                quest = quest,
                done = quest.id in completedQuests,
                onEdit = { questBeingEdited = quest },
                onComplete = { onCompleteQuest(quest.id, quest.reward) }
            )
        }
        item {
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
    }

    if (showCreateQuestDialog) {
        QuestEditorDialog(
            quest = null,
            onDismiss = { showCreateQuestDialog = false },
            onSave = { title, description, tags, difficulty, reward ->
                val nextId = (quests.maxOfOrNull(Quest::id) ?: 0) + 1
                onCreateQuest(Quest(nextId, title, description, tags, difficulty, reward))
                showCreateQuestDialog = false
            }
        )
    }
    questBeingEdited?.let { quest ->
        QuestEditorDialog(
            quest = quest,
            onDismiss = { questBeingEdited = null },
            onSave = { title, description, tags, difficulty, reward ->
                onUpdateQuest(quest.copy(
                    title = title,
                    description = description,
                    tags = tags,
                    difficulty = difficulty,
                    reward = reward
                ))
                questBeingEdited = null
            }
        )
    }
}

@Composable
private fun QuestEditorDialog(
    quest: Quest?,
    onDismiss: () -> Unit,
    onSave: (String, String, List<String>, String, Int) -> Unit
) {
    var title by remember(quest?.id) { mutableStateOf(quest?.title.orEmpty()) }
    var description by remember(quest?.id) { mutableStateOf(quest?.description.orEmpty()) }
    var tags by remember(quest?.id) { mutableStateOf(quest?.tags?.joinToString(", ").orEmpty()) }
    var difficulty by remember(quest?.id) { mutableStateOf(quest?.difficulty ?: "Easy") }
    var rewardText by remember(quest?.id) {
        mutableStateOf(quest?.reward?.toString() ?: "20")
    }
    val reward = rewardText.toIntOrNull()
    val canCreate = title.isNotBlank() && description.isNotBlank() && reward != null && reward > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (quest == null) "Create a sidequest" else "Edit sidequest") },
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
                    onSave(
                        title.trim(),
                        description.trim(),
                        tags.split(",").map(String::trim).filter(String::isNotEmpty),
                        difficulty,
                        validReward
                    )
                },
                enabled = canCreate
            ) {
                Text(if (quest == null) "Create" else "Save")
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
private fun QuestCard(
    modifier: Modifier = Modifier,
    quest: Quest,
    done: Boolean,
    onEdit: () -> Unit,
    onComplete: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !done, onClick = onEdit),
        shape = RoundedCornerShape(QuestsTokens.QuestCardCornerRadius),
        color = if (done) Green100 else CreamSurface,
        shadowElevation = QuestsTokens.Hairline
    ) {
        Row(
            modifier = Modifier.padding(horizontal = QuestsTokens.CardContentInset, vertical = QuestsTokens.CardInset),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = done,
                enabled = !done,
                onCheckedChange = { if (!done) onComplete() }
            )
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
