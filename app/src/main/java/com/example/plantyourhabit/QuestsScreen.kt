package com.example.plantyourhabit

import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

private data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<String>,
    val difficulty: String,
    val reward: Int
)

private val quests = listOf(
    Quest(1, "Book dentist appointment", "Find a nearby clinic and choose a time that works.", listOf("Life admin", "Health"), "Medium", 35),
    Quest(2, "Organize the photo drawer", "Sort loose prints into keep, share, and archive piles.", listOf("Home"), "Medium", 25),
    Quest(3, "Try a new soup recipe", "Pick something seasonal and make enough for tomorrow.", listOf("Someday", "Cooking"), "Easy", 20),
    Quest(4, "Send the postcards", "Write a quick note and drop them in the mailbox.", listOf("Friends"), "Easy", 15)
)

@Composable
fun QuestsScreen(
    completedQuests: Set<Int>,
    onCompleteQuest: (Int, Int) -> Unit,
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
            colors = CardDefaults.cardColors(containerColor = Green200)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionKicker("BACKLOG", Green700)
                    Text(
                        "Small adventures,\nwhenever you're ready.",
                        color = Green900,
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        lineHeight = 27.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "No deadlines here. Pick a sidequest and earn a little gold.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
                Text("🗺️", fontSize = 38.sp)
            }
        }
        Column(
            modifier = Modifier.padding(top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            quests.forEach { quest ->
                val done = quest.id in completedQuests
                QuestCard(quest = quest, done = done) {
                    onCompleteQuest(quest.id, quest.reward)
                }
            }
        }
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green800)
        ) {
            Text("＋  Add a sidequest", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun QuestCard(quest: Quest, done: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !done, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (done) Green100 else CreamSurface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (done) Green700 else Color.Transparent
            ) {
                Text(
                    if (done) "✓" else "",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(top = 2.dp),
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TagChip(quest.difficulty, if (quest.difficulty == "Easy") Green100 else Gold100)
                    quest.tags.forEach { TagChip(it, BeigeBackground) }
                }
                Text(
                    quest.title,
                    color = Green900,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(quest.description, color = TextSecondary, fontSize = 10.sp, lineHeight = 13.sp)
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🪙", color = Gold600, fontSize = 13.sp)
                Text(
                    if (done) "Done" else quest.reward.toString(),
                    color = Gold700,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TagChip(text: String, color: Color) {
    Surface(shape = CircleShape, color = color) {
        Text(
            text.uppercase(),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            color = Green700,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
