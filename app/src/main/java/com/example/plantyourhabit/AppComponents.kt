package com.example.plantyourhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.plantyourhabit.ui.theme.CreamBackground
import com.example.plantyourhabit.ui.theme.CreamSurface
import com.example.plantyourhabit.ui.theme.Gold100
import com.example.plantyourhabit.ui.theme.Gold700
import com.example.plantyourhabit.ui.theme.Green700
import com.example.plantyourhabit.ui.theme.Green900
import com.example.plantyourhabit.ui.theme.TextSecondary

@Composable
fun AppHeader(destination: AppDestinations, gold: Int) {
    val today = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        .format(Date())
        .uppercase(Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CreamBackground)
            .padding(horizontal = AppTokens.HeaderInset, vertical = AppTokens.PageInset),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (destination == AppDestinations.GARDEN) today else "",
                color = TextSecondary,
                fontSize = AppTokens.SupportingText,
                fontWeight = FontWeight.Bold,
                letterSpacing = AppTokens.DateLetterSpacing
            )
            Text(
                text = when (destination) {
                    AppDestinations.GARDEN -> "Garden"
                    AppDestinations.QUESTS -> "Quests"
                    AppDestinations.SHOP -> "Trinket shop"
                },
                color = Green900,
                fontFamily = FontFamily.Serif,
                fontSize = AppTokens.PageTitleText,
                fontWeight = FontWeight.SemiBold
            )
        }
        Surface(
            shape = RoundedCornerShape(AppTokens.CurrencyPillRadius),
            color = Gold100
        ) {
            Row(
                modifier = Modifier.padding(horizontal = AppTokens.CardInset, vertical = AppTokens.ComfortableInset),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🪙", color = Gold700, fontSize = AppTokens.CurrencyText)
                Spacer(Modifier.width(AppTokens.SmallInset))
                Text("$gold", color = Gold700, fontWeight = FontWeight.Bold, fontSize = AppTokens.CardTitleText)
            }
        }
    }
}

@Composable
fun SectionKicker(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = AppTokens.CaptionText,
        fontWeight = FontWeight.Bold,
        letterSpacing = AppTokens.HeadingLetterSpacing
    )
}

@Composable
fun SoftActionButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppTokens.SoftActionButtonHeight),
        shape = RoundedCornerShape(AppTokens.SoftActionButtonCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = CreamSurface,
            contentColor = Green700
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = AppTokens.NoElevation)
    ) {
        Text(text, fontSize = AppTokens.SupportingText, fontWeight = FontWeight.Bold)
    }
}
